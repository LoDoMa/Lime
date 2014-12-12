
local firstInit = true

local this = lime.this.ID
local round = lime.util.round
local newVector = lime.util.vector.new
local hash32 = lime.util.hash32

local tdelta = timeDelta;

local hashes = {}

local function serverUpdate(entityID)

end

local function clientUpdate(entityID)

end

local cnt = 1;

function Lime_Initialize(entityID)
	if firstInit then
		firstInit = false
	end

	lime.entity.set(entityID)

	local px, py = cnt * 4 + 5, 5
	local vx, vy = cnt * -2, 0
	lime.body.position.set(newVector(px, py))
	lime.body.velocity.set(newVector(vx, vy))

	cnt = -cnt
end

function Lime_FrameUpdate(entityID, timeDelta, isActor)
	tdelta = timeDelta

	if lime.network.side.server then serverUpdate(entityID)
	elseif lime.network.side.client then clientUpdate(entityID) end
end

function Lime_GetUnlocalizedName(entityID)
	return "lime.entity.ball.name"
end
