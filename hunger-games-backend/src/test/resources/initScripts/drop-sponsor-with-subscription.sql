UPDATE sponsors set news_subscription_order_order_id=null where user_id='523adea8-9f98-41a3-bae0-1e4875aceaae';
delete from news_subscription_orders where order_id = '9e33833d-34d2-46c6-8354-d8f6e05f4410';
delete from orders where order_id = '9e33833d-34d2-46c6-8354-d8f6e05f4410';
delete from sponsors where user_id = '523adea8-9f98-41a3-bae0-1e4875aceaae';
delete from user_roles where user_id = '523adea8-9f98-41a3-bae0-1e4875aceaae';
delete from users where user_id = '523adea8-9f98-41a3-bae0-1e4875aceaae';
delete from settings where setting_id='3770eb1d-76c7-418c-b2fa-9948f381aac7';
