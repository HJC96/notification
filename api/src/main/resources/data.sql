INSERT INTO user_notification_metadata (
    user_id, device_token, platform, phone_number, email,
    notification_enabled, push_enabled, sms_enabled, email_enabled, last_updated_at
) VALUES (
    1, 'test-device-token', 'ANDROID', '010-1234-5678', 'test@example.com',
    true, true, false, false, CURRENT_TIMESTAMP
);

INSERT INTO user_notification_metadata (
    user_id, device_token, platform, phone_number, email,
    notification_enabled, push_enabled, sms_enabled, email_enabled, last_updated_at
) VALUES (
    2, 'another-device-token', 'IOS', '010-9876-5432', 'another@example.com',
    true, false, true, true, CURRENT_TIMESTAMP
);