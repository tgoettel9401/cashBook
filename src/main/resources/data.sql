insert into cash_book (id, name, account_number) values (1, 'Some name', 'DE48293837817');

insert into cash_book_entry (id, booking_date, booking_text, cash_partner_account_number, cash_partner_bank_code,
cash_partner_name, created_at, purpose, value, value_date)
values (
1, -- ID
'2020-06-20', -- BookingDate
'Some booking text', -- BookingText
'cashPartnerAccountNumber', -- CashPartnerAccountNumber
'cashPartnerBankCode', -- CashPartnerBankCode
'cashPartnerName', -- CashPartnerName
'2020-06-02 17:22:00.0', --CreatedAt
'purpose is different', -- Purpose
'17.20', -- Value
'2020-06-20' -- ValueDate
);