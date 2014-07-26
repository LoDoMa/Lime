
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
		lime.body.translation.set(newVector(1.0, 7.0))

		lime.body.set(hashes["body"])
		lime.body.translation.set(newVector(1.0, 7.0))

		lime.listener.set("Lime::onNewUser", onNewUser)
	end
end

local function clientUpdate()
	lime.entity.set(this)

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
