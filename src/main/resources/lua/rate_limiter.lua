
-- local tokens = tonumber(redis.call('hget', KEYS[1], 'tokens'))
-- local timestamp = tonumber(redis.call('hget', KEYS[1], 'timestamp'))

-- -- 1. Khởi tạo Bucket nếu chưa tồn tại
-- if not tokens then
--     -- Đặt số token ban đầu bằng capacity, và timestamp hiện tại
--     redis.call('hmset', KEYS[1], 'tokens', ARGV[1], 'timestamp', ARGV[3])
--     redis.call('expire', KEYS[1], 3600) -- Thiết lập thời gian hết hạn (ví dụ: 1 giờ)
--     return 1 -- Token được cấp
-- end

-- -- 2. Tính toán số token mới được nạp vào dựa trên thời gian trôi qua
-- local elapsed = ARGV[3] - timestamp
-- local refill_tokens = elapsed * tonumber(ARGV[2])

-- -- 3. Cập nhật số token hiện có (không vượt quá Capacity)
-- local new_tokens = math.min(tonumber(ARGV[1]), tokens + refill_tokens)

-- local tokens_to_consume = tonumber(ARGV[4])

-- -- 4. Kiểm tra và tiêu thụ token
-- if new_tokens >= tokens_to_consume then
--     -- Được phép: Sử dụng token và cập nhật timestamp mới
--     redis.call('hmset', KEYS[1], 'tokens', new_tokens - tokens_to_consume, 'timestamp', ARGV[3])
--     return 1 
-- else
--     -- Bị chặn: Chỉ cập nhật timestamp để tính toán refill lần sau. 
--     -- Không tiêu thụ token nào.
--     redis.call('hmset', KEYS[1], 'timestamp', ARGV[3])
--     return 0 
-- end