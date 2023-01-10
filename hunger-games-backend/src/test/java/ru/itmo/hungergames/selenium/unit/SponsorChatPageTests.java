package ru.itmo.hungergames.selenium.unit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.itmo.hungergames.model.entity.chat.Chat;
import ru.itmo.hungergames.model.entity.chat.Message;
import ru.itmo.hungergames.model.entity.order.OrderDetail;
import ru.itmo.hungergames.model.entity.order.Resource;
import ru.itmo.hungergames.model.entity.order.ResourceOrder;
import ru.itmo.hungergames.model.entity.user.*;
import ru.itmo.hungergames.model.request.OrderDetailRequest;
import ru.itmo.hungergames.model.request.PaymentRequest;
import ru.itmo.hungergames.model.request.ResourceOrderRequest;
import ru.itmo.hungergames.model.response.*;
import ru.itmo.hungergames.selenium.pages.ChatPage;
import ru.itmo.hungergames.selenium.pages.SponsorCreateOrderPage;
import ru.itmo.hungergames.selenium.util.SeleniumTest;
import ru.itmo.hungergames.selenium.util.SeleniumTestBase;
import ru.itmo.hungergames.service.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SeleniumTest
public class SponsorChatPageTests extends SeleniumTestBase {
    private ChatPage page;
    private String sourceUrl;
    private String sourceWindow;
    private final Sponsor sponsor = Sponsor.builder()
            .id(new UUID(42, 42))
            .username("sponsor")
            .name("sponsor")
            .userRoles(Set.of(UserRole.SPONSOR))
            .build();

    private final Mentor mentor = Mentor.builder()
            .id(new UUID(42, 43))
            .name("mentor")
            .build();

    private final Tribute tribute = Tribute.builder()
            .id(new UUID(42, 44))
            .name("tribute")
            .mentor(mentor)
            .userRoles(Set.of(UserRole.TRIBUTE))
            .avatarUri("https://upload.wikimedia.org/wikipedia/commons/thumb/6/68/Orange_tabby_cat_sitting_on_fallen_leaves-Hisashi-01A.jpg/800px-Orange_tabby_cat_sitting_on_fallen_leaves-Hisashi-01A.jpg")
            .build();

    private final Resource resource = Resource.builder()
            .id(new UUID(42, 42))
            .name("resource")
            .price(new BigDecimal(42))
            .build();

    private final OrderDetail orderDetail = OrderDetail.builder()
            .size(1)
            .resource(resource)
            .build();

    private final ResourceOrder orderPendingApproval = ResourceOrder.builder()
            .id(new UUID(42, 0))
            .tribute(tribute)
            .approved(null)
            .paid(true)
            .orderDetails(List.of(orderDetail))
            .build();

    private final ResourceOrder orderPendingPayment = ResourceOrder.builder()
            .id(new UUID(42, 1))
            .price(orderDetail.getTotal())
            .tribute(tribute)
            .approved(true)
            .paid(false)
            .orderDetails(List.of(orderDetail))
            .build();

    private final ResourceOrder orderRejected = ResourceOrder.builder()
            .id(new UUID(42, 2))
            .tribute(tribute)
            .approved(false)
            .paid(true)
            .orderDetails(List.of(orderDetail))
            .build();

    private final ResourceOrder orderApprovedAndPaid = ResourceOrder.builder()
            .id(new UUID(42, 3))
            .tribute(tribute)
            .approved(true)
            .paid(true)
            .orderDetails(List.of(orderDetail))
            .build();

    private final List<Message> messages = List.of(
            Message.builder()
                    .message("test")
                    .user(tribute)
                    .build(),

            Message.builder()
                    .message("/" + orderPendingApproval.getId())
                    .user(tribute)
                    .build(),

            Message.builder()
                    .message("/" + orderPendingPayment.getId())
                    .user(tribute)
                    .build(),

            Message.builder()
                    .message("/" + orderRejected.getId())
                    .user(tribute)
                    .build(),

            Message.builder()
                    .message("/" + orderApprovedAndPaid.getId())
                    .user(tribute)
                    .build()
    );

    private final Chat chat = Chat.builder()
            .id(new UUID(42, 42))
            .sponsor(sponsor)
            .tribute(tribute)
            .messages(messages)
            .build();

    @MockBean
    private ChatService chatService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private TributeService tributeService;

    @MockBean
    private MentorService mentorService;

    @MockBean
    private ResourceService resourceService;

    @MockBean
    private SponsorService sponsorService;

    @MockBean
    private PaymentManagerService paymentManagerService;

    private List<ChatPage.Message> actualMessages;

    @BeforeEach
    public void setUp() {
        var sponsor = User.builder()
                .id(new UUID(42, 42))
                .username("sponsor")
                .name("sponsor")
                .build();
        this.authenticate(sponsor, UserRole.SPONSOR);

        doReturn(List.of(new ChatResponse(this.chat))).when(this.chatService).getChatsByUserId();
        doReturn(this.messages.stream().map(MessageResponse::new).collect(Collectors.toList())).when(this.chatService).getMessagesByChatId(this.chat.getId());
        doReturn(new OrderResponse(this.orderPendingApproval)).when(this.orderService).getOrderById(this.orderPendingApproval.getId());
        doReturn(new OrderResponse(this.orderPendingPayment)).when(this.orderService).getOrderById(this.orderPendingPayment.getId());
        doReturn(new OrderResponse(this.orderRejected)).when(this.orderService).getOrderById(this.orderRejected.getId());
        doReturn(new OrderResponse(this.orderApprovedAndPaid)).when(this.orderService).getOrderById(this.orderApprovedAndPaid.getId());
        doReturn(new TributeResponse(this.tribute)).when(this.tributeService).getTributeById(this.tribute.getId());
        doReturn(new MentorResponse(this.mentor)).when(this.mentorService).getMentorById(this.mentor.getId());
        doReturn(List.of(this.resource)).when(this.resourceService).getAllResources();

        this.get("/sponsor/chat/" + this.chat.getId());

        page = new ChatPage(this.driver);
        PageFactory.initElements(driver, page);

        this.waitForAngularRequests();

        this.page.getMessageInput().clear();

        this.actualMessages = this.page.getMessages();

        sourceUrl = driver.getCurrentUrl();
        sourceWindow = driver.getWindowHandle();
    }

    @AfterEach
    public void tearDown() {
        this.closePaymentWindows(this.sourceWindow);
    }

    @Test
    public void showTextMessage() {
        Assertions.assertEquals(this.messages.get(0).getMessage(), this.actualMessages.get(0).getText());
        Assertions.assertEquals(this.messages.get(0).getUser().getName(), this.actualMessages.get(0).getSender());
    }

    @Test
    public void showOrderPendingApproval() {
        var message = this.actualMessages.get(1);
        Assertions.assertEquals("Ожидает рассмотрения ментором", message.orderStatus());
        Assertions.assertEquals(List.of(this.orderDetail.getSize() + "X " + this.resource.getName()), message.orderDetails());
    }

    @Test
    public void showOrderPendingPayment() {
        var message = this.actualMessages.get(2);
        Assertions.assertEquals("Ожидает оплаты", message.orderStatus());
        Assertions.assertEquals(List.of(this.orderDetail.getSize() + "X " + this.resource.getName()), message.orderDetails());
        Assertions.assertEquals(this.orderDetail.getTotal(), message.getOrderSum());
    }

    @Test
    public void showOrderRejected() {
        var message = this.actualMessages.get(3);
        Assertions.assertEquals("Отклонен ментором", message.orderStatus());
        Assertions.assertEquals(List.of(this.orderDetail.getSize() + "X " + this.resource.getName()), message.orderDetails());
    }

    @Test
    public void showOrderApprovedAndPaid() {
        var message = this.actualMessages.get(4);
        Assertions.assertEquals("Отправлен", message.orderStatus());
        Assertions.assertEquals(List.of(this.orderDetail.getSize() + "X " + this.resource.getName()), message.orderDetails());
    }

    @Test
    public void payOrderMessage() {
        this.actualMessages.get(2).pay();

        this.switchToNewWindow(this.sourceWindow);
        assertThat(this.driver.getCurrentUrl(), endsWith("#/capitol/payment?id=" + this.orderPendingPayment.getId()));
    }

    @Test
    public void sendMessage() {
        final var message = "test message";

        var messageEntity = Message.builder()
                .message(message)
                .user(this.sponsor)
                .build();

        doReturn(new MessageResponse(messageEntity))
                .when(this.chatService)
                .sendMessage(eq(this.chat.getId()), argThat(messageRequest -> messageRequest.getMessage().equals(message)));

        this.page.getMessageInput().sendKeys(message);
        this.page.getMessageSendButton().click();

        this.waitForAngularRequests();

        verify(this.chatService, times(1))
                .sendMessage(eq(this.chat.getId()), argThat(messageRequest -> messageRequest.getMessage().equals(message)));

        Assertions.assertEquals(message, this.page.getMessages().get(5).getText());
    }

    @Test
    public void sendResources() {
        this.page.getSendResourcesButton().click();

        this.redirectWait(this.sourceUrl);

        assertThat(driver.getCurrentUrl(), endsWith("#/sponsor/chat/" + this.chat.getId() + "/" + this.tribute.getId() + "/createorder"));

        final var createOrderPage = new SponsorCreateOrderPage(this.driver);
        PageFactory.initElements(this.driver, createOrderPage);

        var resourceAmountInput = createOrderPage.getResourceRows().get(0).getAmountInput();
        resourceAmountInput.clear();
        resourceAmountInput.sendKeys("1");

        var expectedOrderRequest = ResourceOrderRequest.builder()
                .tributeId(this.tribute.getId())
                .orderDetails(List.of(
                        new OrderDetailRequest(this.resource.getId(), 1)
                ))
                .build();

        final var orderId = new UUID(1, 2);

        doReturn(ResourceOrderResponse.builder().orderId(orderId).build())
                .when(this.sponsorService).sendResourcesForApproval(expectedOrderRequest);

        doNothing().when(this.paymentManagerService).approvePayment(new PaymentRequest(orderId));

        final var message = "/" + orderId;

        var messageEntity = Message.builder()
                .message(message)
                .user(this.sponsor)
                .build();

        doReturn(new MessageResponse(messageEntity))
                .when(this.chatService)
                .sendMessage(eq(this.chat.getId()), argThat(messageRequest -> messageRequest.getMessage().equals(message)));

        createOrderPage.getPayButton().click();

        this.approvePayment(this.sourceWindow);

        new WebDriverWait(this.driver, Duration.ofSeconds(3)).until(ExpectedConditions.numberOfElementsToBe(By.tagName("li"), 6));

        verify(this.chatService, times(1))
                .sendMessage(eq(this.chat.getId()), argThat(messageRequest -> messageRequest.getMessage().equals(message)));
    }
}
