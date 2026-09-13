INSERT INTO notification_channel_preferences (user_id, notification_type, channel, enabled)
SELECT u.id, 'SOCIAL', c.channel, TRUE
FROM users u
CROSS JOIN (VALUES ('EMAIL'), ('SMS'), ('WHATSAPP')) AS c(channel)
ON CONFLICT (user_id, notification_type, channel) DO NOTHING;
