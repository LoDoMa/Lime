
local firstInit = true

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

local function serverUpdate(entityID)

end

local function clientUpdate(entityID)
	limex.follow(entityID, hashes["body"], entityID, hashes["m_body"])
end

function Lime_Initialize(entityID)
	if firstInit then
		loadHashes()

		firstInit = false
	end

	lime.entity.set(entityID)
	
	lime.hitbox.collider.set(hashes["body"])
	local px, py = math.random() * 10 + 10, math.random() * 10 + 10
	local vx, vy = math.random() * 2 - 1, math.random() * 2 - 1
	lime.hitbox.collider.transform.position.set(newVector(px, py))
	lime.hitbox.collider.velocity.set(newVector(vx, vy))
end

function Lime_FrameUpdate(entityID, timeDelta, isActor)
	tdelta = timeDelta

	if lime.network.side.server then serverUpdate(entityID)
	elseif lime.network.side.client then clientUpdate(entityID) end
end

function Lime_GetUnlocalizedName(entityID)
	return "lime.entity.ball.name"
end
