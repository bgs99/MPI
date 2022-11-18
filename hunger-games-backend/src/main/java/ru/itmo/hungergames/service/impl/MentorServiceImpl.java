package ru.itmo.hungergames.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.hungergames.model.entity.Mentor;
import ru.itmo.hungergames.model.entity.Orders;
import ru.itmo.hungergames.model.entity.OrdersType;
import ru.itmo.hungergames.model.request.ApproveResourcesRequest;
import ru.itmo.hungergames.model.response.ResourceApprovalResponse;
import ru.itmo.hungergames.repository.MentorRepository;
import ru.itmo.hungergames.repository.OrdersRepository;
import ru.itmo.hungergames.service.MentorService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MentorServiceImpl implements MentorService {
    private final MentorRepository mentorRepository;
    private final OrdersRepository ordersRepository;

    @Autowired
    public MentorServiceImpl(MentorRepository mentorRepository, OrdersRepository ordersRepository) {
        this.mentorRepository = mentorRepository;
        this.ordersRepository = ordersRepository;
    }

    @Override
    public List<Mentor> getAllMentors() {
        return mentorRepository.findAll();
    }

    @Override
    public List<ResourceApprovalResponse> getOrdersForApproval(Long mentorId) {
        List<Orders> orders = ordersRepository
                .findAllByTribute_MentorIdAndPaidAndApprovedAndOrdersType(mentorId, true, null, OrdersType.RESOURCES);
        return orders.stream().map(ResourceApprovalResponse::new).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void approveResourcesToSend(ApproveResourcesRequest approveResourcesRequest) {
        Mentor mentor = mentorRepository
                .findById(approveResourcesRequest.getMentorId())
                .orElseThrow(() -> new ResourceNotFoundException("There's no mentor with such ID"));
        Optional<Orders> ordersOptional = ordersRepository
                .findByIdAndTribute_MentorIdAndPaid(
                        approveResourcesRequest.getOrderId(), mentor.getId(), true);
        if (!ordersOptional.isPresent()) {
            return;
        }
        Orders orders = ordersOptional.get();
        orders.setApproved(approveResourcesRequest.getApproved());
        ordersRepository.save(orders);
    }
}
