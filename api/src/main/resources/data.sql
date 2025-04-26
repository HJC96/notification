-- user_notification_metadata 데이터

INSERT INTO user_notification_metadata (
    user_id, device_token, platform, phone_number, email,
    notification_enabled, push_enabled, sms_enabled, email_enabled, last_updated_at
) VALUES
(1, 'test-device-token', 'ANDROID', '010-1234-5678', 'test@example.com', true, true, false, false, CURRENT_TIMESTAMP),
(2, 'another-device-token', 'IOS', '010-9876-5432', 'another@example.com', true, false, true, true, CURRENT_TIMESTAMP),
(3, 'third-device-token', 'ANDROID', '010-5555-5555', 'third@example.com', true, true, true, true, CURRENT_TIMESTAMP);

-- email_template 데이터

INSERT INTO email_template (template_key, template_content) VALUES
('email-template:default', '<html><body>{{body}}</body></html>'),
('email-template:welcome', '<html><body>Welcome, {{name}}!</body></html>'),
('email-template:reset-password', '<html><body>Reset your password <a href=\"{{link}}\">here</a>.</body></html>');