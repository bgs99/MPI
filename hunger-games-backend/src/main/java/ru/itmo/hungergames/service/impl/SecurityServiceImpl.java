package ru.itmo.hungergames.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.hungergames.model.entity.Sponsor;
import ru.itmo.hungergames.model.entity.User;
import ru.itmo.hungergames.model.entity.UserRole;
import ru.itmo.hungergames.model.response.JwtResponse;
import ru.itmo.hungergames.repository.SponsorRepository;
import ru.itmo.hungergames.repository.UserRepository;
import ru.itmo.hungergames.service.SecurityService;
import ru.itmo.hungergames.util.JwtUtil;
import ru.itmo.hungergames.util.SecurityUtil;

import java.util.Set;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
public class SecurityServiceImpl implements SecurityService {
    private final AuthenticationProvider authenticationProvider;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;
    private final SecurityUtil securityUtil;
    private final SponsorRepository sponsorRepository;

    @Autowired
    public SecurityServiceImpl(AuthenticationProvider authenticationProvider, JwtUtil jwtUtil, BCryptPasswordEncoder encoder, UserRepository userRepository, SecurityUtil securityUtil, SponsorRepository sponsorRepository) {
        this.authenticationProvider = authenticationProvider;
        this.jwtUtil = jwtUtil;
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.securityUtil = securityUtil;
        this.sponsorRepository = sponsorRepository;
    }

    @Override
    public JwtResponse authenticateUser(User user) {
        Authentication authentication = authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        User userDetails = (User) authentication.getPrincipal();
        Set<String> roles = userDetails
                .getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return new JwtResponse(
                userDetails.getId(),
                jwtUtil.generateJwtToken(userDetails),
                userDetails.getUsername(),
                roles
        );
    }

    @Override
    @Transactional
    public void createUser(User user) {
        securityUtil.validateBeforeSigningUp(user);
        user.setPassword(encoder.encode(user.getPassword()));
        user.getUserRoles().add(UserRole.SPONSOR);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void createSponsor(Sponsor sponsor) {
        securityUtil.validateBeforeSigningUp(sponsor);
        sponsor.setPassword(encoder.encode(sponsor.getPassword()));
        sponsor.getUserRoles().add(UserRole.SPONSOR);
        sponsorRepository.save(sponsor);
    }
}
