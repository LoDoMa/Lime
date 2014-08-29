
local firstUpdate = true

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

local function serverUpdate(entityID)
	if firstUpdate then
		lime.entity.set(entityID)

		--[[
		lime.body.set(hashes["head"])
		lime.body.transform.position.set(newVector(16.0, 7.0))
		lime.body.transform.rotation.set(0)
		lime.body.transform.push()

		lime.body.set(hashes["body"])
		lime.body.transform.position.set(newVector(16.0, 7.0))
		lime.body.transform.rotation.set(0)
		lime.body.transform.push()
		]]
	end
end

local function clientUpdate(entityID, timeDelta, isActor)
	--[[
	if isActor then -- buggy movement
		lime.entity.set(entityID)
		lime.body.set(hashes["body"])
		if lime.input.keyboard.get(lime.input.keyboard.key.a) then
			lime.body.force(newVector(-100.0 * timeDelta, 0.0), newVector(0.2, 0.5)) -- buggy left
		end
		if lime.input.keyboard.get(lime.input.keyboard.key.d) then
			lime.body.force(newVector(100.0 * timeDelta, 0.0), newVector(0.2, 0.5)) -- buggy right
		end
		if lime.input.keyboard.get(lime.input.keyboard.key.w) then
			lime.body.force(newVector(0.0, 20.0 * timeDelta), newVector(0.2, 0.5)) -- buggy up
		end
		if lime.input.keyboard.get(lime.input.keyboard.key.s) then
			lime.body.force(newVector(0.0, -20.0 * timeDelta), newVector(0.2, 0.5)) -- buggy down
		end
	end
	]]

	--lime.entity.set(entityID)

	--limex.follow(entityID, hashes["head"], entityID, hashes["m_head"])
	--limex.follow(entityID, hashes["body"], entityID, hashes["m_body"])

	if firstUpdate then
		lime.light.basic.add(0, newVector(16, 15), 40, newColor(1, 1, 1, 1), -1, 361)
	end
end

function Lime_FrameUpdate(entityID, timeDelta, isActor)
	tdelta = timeDelta

	if firstUpdate then
		loadHashes()
	end

	if lime.network.side.server then serverUpdate(entityID)
	elseif lime.network.side.client then clientUpdate(entityID, timeDelta, isActor) end

	firstUpdate = false
end

function Lime_GetUnlocalizedName()
	return "lime.entity.zombie.name"
end
