
local firstInit = true

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
	addHash("body")
	addHash("m_body")
end

local function serverUpdate(entityID)

end

local function clientUpdate(entityID, timeDelta, isActor)
	limex.follow(entityID, hashes["body"], entityID, hashes["m_body"])
end

function Lime_Initialize(entityID)
	if firstInit then
		loadHashes()

		firstInit = false

		if lime.network.side.client then
			lime.light.basic.add(0, newVector(16, 15), 40, newColor(1, 1, 1, 1), -1, 361)
		end
	end

	lime.entity.set(entityID)

	lime.hitbox.collider.set(hashes["body"])
	lime.hitbox.collider.transform.position.set(newVector(5.0, 5.0))
end

function Lime_FrameUpdate(entityID, timeDelta, isActor)
	tdelta = timeDelta

	if lime.network.side.server then serverUpdate(entityID)
	elseif lime.network.side.client then clientUpdate(entityID, timeDelta, isActor) end
end

function Lime_GetUnlocalizedName(entityID)
	return "lime.entity.zombie.name"
end
