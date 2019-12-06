select * from dual;

-- load test customers
insert into customer(id, name) values(1, 'First Company');
insert into customer(id, name) values(2, 'Big Company');
insert into customer(id, name) values(3, 'Test Company');

-- load test events
insert into event(id, event_type, quantity, cost, customer_id) values (1, 'Call', 4.0, 25, 1);
insert into event(id, event_type, quantity, cost, customer_id) values (2, 'Call', 1.0, 10, 1);
insert into event(id, event_type, quantity, cost, customer_id) values (3, 'SMS', 2.0, 45, 1);
insert into event(id, event_type, quantity, cost, customer_id) values (4, 'Call', 6.0, 21, 1);
insert into event(id, event_type, quantity, cost, customer_id) values (5, 'Call', 2.0, 15, 1);

insert into event(id, event_type, quantity, cost, customer_id) values (6, 'Call', 2.0, 15, 2);
insert into event(id, event_type, quantity, cost, customer_id) values (7, 'SMS', 2.0, 15, 2);
insert into event(id, event_type, quantity, cost, customer_id) values (8, 'Call', 2.0, 15, 2);
insert into event(id, event_type, quantity, cost, customer_id) values (9, 'SMS', 2.0, 15, 2);
insert into event(id, event_type, quantity, cost, customer_id) values (10, 'Call', 2.0, 15, 2);
insert into event(id, event_type, quantity, cost, customer_id) values (11, 'SMS', 2.0, 15, 2);
