UPDATE sponsors set news_subscription_order_order_id=null where user_id='4a9f1d37-c6fd-4391-8082-655bb98fb460';
delete from news_subscription_orders where order_id = '9e33833d-34d2-46c6-8354-d8f6e05f4410';
delete from orders where order_id = '9e33833d-34d2-46c6-8354-d8f6e05f4410';
delete from sponsors where user_id = '4a9f1d37-c6fd-4391-8082-655bb98fb460';
delete from user_roles where user_id = '4a9f1d37-c6fd-4391-8082-655bb98fb460';
delete from users where user_id = '4a9f1d37-c6fd-4391-8082-655bb98fb460';
