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
import ru.itmo.hungergames.model.entity.user.Settings;
import ru.itmo.hungergames.model.entity.user.Sponsor;
import ru.itmo.hungergames.model.entity.user.User;
import ru.itmo.hungergames.model.entity.user.UserRole;
import ru.itmo.hungergames.model.response.JwtResponse;
import ru.itmo.hungergames.repository.SettingsRepository;
import ru.itmo.hungergames.repository.SponsorRepository;
import ru.itmo.hungergames.repository.UserRepository;
import ru.itmo.hungergames.security.UserDetailsServiceImpl;
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
    private final UserDetailsServiceImpl userDetailsService;
    private final SettingsRepository settingsRepository;

    @Autowired
    public SecurityServiceImpl(AuthenticationProvider authenticationProvider,
                               JwtUtil jwtUtil,
                               BCryptPasswordEncoder encoder,
                               UserRepository userRepository,
                               SecurityUtil securityUtil,
                               SponsorRepository sponsorRepository,
                               UserDetailsServiceImpl userDetailsService,
                               SettingsRepository settingsRepository) {
        this.authenticationProvider = authenticationProvider;
        this.jwtUtil = jwtUtil;
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.securityUtil = securityUtil;
        this.sponsorRepository = sponsorRepository;
        this.userDetailsService = userDetailsService;
        this.settingsRepository = settingsRepository;
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
                roles,
                userDetails.getName()
        );
    }

    @Override
    @Transactional
    public void createUser(User user) {
        securityUtil.validateBeforeSigningUp(user);
        user.setPassword(encoder.encode(user.getPassword()));
        user.getUserRoles().add(UserRole.SPONSOR);
        Settings settings = new Settings();
        user.setSettings(settings);
        settingsRepository.save(settings);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void createSponsor(Sponsor sponsor) {
        securityUtil.validateBeforeSigningUp(sponsor);
        sponsor.setPassword(encoder.encode(sponsor.getPassword()));
        sponsor.getUserRoles().add(UserRole.SPONSOR);
        Settings settings = new Settings();
        sponsor.setSettings(settings);
        settingsRepository.save(settings);
        sponsorRepository.save(sponsor);
    }

    @Override
    public JwtResponse authenticateTributeAndMentorAndModerator(User user) {
        User userDetails = (User)userDetailsService.loadUserByUsername(user.getUsername());
        Set<String> roles = userDetails
                .getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        Authentication authentication;
        if (roles.contains(UserRole.MENTOR.toString()) ||
                roles.contains(UserRole.TRIBUTE.toString()) ||
                roles.contains(UserRole.MODERATOR.toString())) {
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
}
