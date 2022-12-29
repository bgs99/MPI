package ru.itmo.hungergames.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.hungergames.model.entity.News;
import ru.itmo.hungergames.model.entity.order.AdvertisementOrder;
import ru.itmo.hungergames.model.entity.user.Moderator;
import ru.itmo.hungergames.model.entity.user.User;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.model.request.ApproveAdvertisingTextRequest;
import ru.itmo.hungergames.model.request.NewsRequest;
import ru.itmo.hungergames.model.response.AdvertisingTextResponse;
import ru.itmo.hungergames.model.response.JwtResponse;
import ru.itmo.hungergames.model.response.UserResponse;
import ru.itmo.hungergames.repository.AdvertisementOrderRepository;
import ru.itmo.hungergames.repository.ModeratorRepository;
import ru.itmo.hungergames.repository.NewsRepository;
import ru.itmo.hungergames.security.UserDetailsServiceImpl;
import ru.itmo.hungergames.service.ModeratorService;
import ru.itmo.hungergames.util.JwtUtil;
import ru.itmo.hungergames.util.SecurityUtil;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ModeratorServiceImpl implements ModeratorService {
    private final ModeratorRepository moderatorRepository;
    private final NewsRepository newsRepository;
    private final AdvertisementOrderRepository advertisementOrderRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationProvider authenticationProvider;
    private final JwtUtil jwtUtil;
    private final SecurityUtil securityUtil;

    @Autowired
    public ModeratorServiceImpl(ModeratorRepository moderatorRepository,
                                NewsRepository newsRepository,
                                AdvertisementOrderRepository advertisementOrderRepository,
                                UserDetailsServiceImpl userDetailsService,
                                AuthenticationProvider authenticationProvider,
                                JwtUtil jwtUtil,
                                SecurityUtil securityUtil) {
        this.moderatorRepository = moderatorRepository;
        this.newsRepository = newsRepository;
        this.advertisementOrderRepository = advertisementOrderRepository;
        this.userDetailsService = userDetailsService;
        this.authenticationProvider = authenticationProvider;
        this.jwtUtil = jwtUtil;
        this.securityUtil = securityUtil;
    }

    @Override
    @Transactional
    public void publishNews(NewsRequest newsRequest) {
        Moderator moderator = moderatorRepository
                .findById(securityUtil.getAuthenticatedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("There's no moderator with such ID"));
        newsRepository.save(News.builder()
                .name(newsRequest.getName())
                .content(newsRequest.getContent())
                .moderator(moderator)
                .build());
    }

    @Override
    public List<UserResponse> getAllModerators() {
        return moderatorRepository
                .findAll().stream()
                .map(UserResponse::new)
                .toList();
    }

    @Override
    public JwtResponse authenticateModerator(User user) {
        User userDetails = (User)userDetailsService.loadUserByUsername(user.getUsername());
        Set<String> roles = userDetails
                .getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        Authentication authentication;
        if (roles.contains(UserRole.MODERATOR.toString())) {
            authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), userDetails.getAuthorities());
        } else {
            authentication = authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        }
        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        return new JwtResponse(
                userDetails.getId(),
                jwtUtil.generateJwtToken(userDetails),
                userDetails.getUsername(),
                roles,
                userDetails.getName()
        );
    }

    @Override
    public AdvertisingTextResponse getAnotherAdvertisingText() {
        return new AdvertisingTextResponse(advertisementOrderRepository
                .findFirstByApproved(false)
                .orElseThrow(() -> new ResourceNotFoundException("There's no advertising text")));
    }

    @Override
    @Transactional
    public void approveAdvertisingText(ApproveAdvertisingTextRequest approveAdvertisingTextRequest) {
        AdvertisementOrder advertisementOrder = advertisementOrderRepository
                .findById(approveAdvertisingTextRequest.getAdvertisingTextId())
                .orElseThrow(() -> new ResourceNotFoundException("There's no advertising text with the ID"));

        advertisementOrder.setApproved(approveAdvertisingTextRequest.isApproved());
        advertisementOrderRepository.save(advertisementOrder);
    }
}
