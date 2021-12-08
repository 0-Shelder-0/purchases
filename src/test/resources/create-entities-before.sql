delete from purchasestest.products;
delete from purchasestest.users;

insert into purchasestest.users (Id, Login, Password, Email)
values (1, 'test', 'test', 'test@email.com');

insert into purchasestest.products (Id, Description, IsCompleted, UserId)
values (1, 'prod', false, 1);