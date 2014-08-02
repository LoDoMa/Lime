
local firstUpdate = true

local this = lime.this.ID
local round = lime.util.round
local newVector = lime.util.vector.new
local hash32 = lime.util.hash32

local tdelta = timeDelta;

local hashes = {}

local function addHash(str)
	hashes[str] = hash32(str)
end

local function loadHashes()
	addHash("body")
	addHash("m_body")
end

local function serverUpdate()
	if firstUpdate then
		lime.entity.set(this)

		lime.body.set(hashes["body"])
		lime.body.transform.position.set(newVector(16.0, 7.0))
		lime.body.transform.rotation.set(0)
		lime.body.transform.push()
	end
end

local function clientUpdate()
	lime.entity.set(this)
	
	limex.follow(this, hashes["body"], this, hashes["m_body"])
end

function Lime_FrameUpdate(timeDelta, isActor)
	tdelta = timeDelta

	if firstUpdate then
		loadHashes()
	end

	if lime.network.side.server then serverUpdate()
	elseif lime.network.side.client then clientUpdate() end

	firstUpdate = false
end
