
local firstUpdate = true

local this = lime.this.ID
local round = lime.util.round
local newVector = lime.util.vector.new
local hash32 = lime.util.hash32

local hashes = {}

local function addHash(str)
	hashes[str] = hash32(str)
end

local function loadHashes()
	addHash("head")
	addHash("body")
	addHash("m_head")
	addHash("m_body")
end

local function onNewUser()
	print("new user")
end

local function serverUpdate()
	if firstUpdate then
		lime.entity.set(this)

		lime.body.set(hashes["head"])
		lime.body.impulse.linear(newVector(100, 0), newVector(0, 0))

		lime.listener.set("Lime::onNewUser", onNewUser)
	end
end

local function clientUpdate()
	lime.entity.set(this)

	lime.body.set(hashes["head"])
	local translation = lime.body.translation.get()
	--print(round(translation.x, 2) .. " " .. round(translation.y, 2))

	limex.follow(this, hashes["head"], this, hashes["m_head"])
	limex.follow(this, hashes["body"], this, hashes["m_body"])
end

function Lime_FrameUpdate(timeDelta, isActor, side)
	if firstUpdate then
		loadHashes()
	end

	if side == lime.netside.server then
		serverUpdate()
	elseif side == lime.netside.client then
		clientUpdate()
	end

	firstUpdate = false
end
