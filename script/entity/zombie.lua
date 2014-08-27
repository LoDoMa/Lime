
local firstUpdate = true

local this = lime.this.ID
local round = lime.util.round
local newVector = lime.util.vector.new
local newColor = lime.util.color.new
local hash32 = lime.util.hash32

local tdelta = timeDelta;

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

local function serverUpdate()
	if firstUpdate then
		lime.entity.set(this)

		lime.body.set(hashes["head"])
		lime.body.transform.position.set(newVector(16.0, 7.0))
		lime.body.transform.rotation.set(0)
		lime.body.transform.push()

		lime.body.set(hashes["body"])
		lime.body.transform.position.set(newVector(16.0, 7.0))
		lime.body.transform.rotation.set(0)
		lime.body.transform.push()
	end
end

local function clientUpdate()
	lime.entity.set(this)

	limex.follow(this, hashes["head"], this, hashes["m_head"])
	limex.follow(this, hashes["body"], this, hashes["m_body"])

	if firstUpdate then
		lime.light.basic.add(0, newVector(16, 15), 40, newColor(1, 1, 1, 1), -1, 361)
	end
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

function Lime_GetUnlocalizedName()
	return "lime.entity.zombie.name"
end
