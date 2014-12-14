
local firstInit = true

local this = lime.this.ID
local round = lime.util.round
local newVector = lime.util.vector.new
local hash32 = lime.util.hash32

local function serverUpdate(entityID, timeDelta, isActor)

end

local function serverPostUpdate(entityID, timeDelta, isActor)

end

local function clientUpdate(entityID, timeDelta, isActor)
	if lime.input.keyboard.getDown(lime.input.keyboard.key.left) then
		print("key press test")
	end
end

local function clientPostUpdate(entityID, timeDelta, isActor)
	lime.entity.set(entityID)

	lime.shape.component.set(0)
	lime.shape.component.position.set(lime.body.position.get())
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

function Lime_Update(entityID, timeDelta, isActor)
	if lime.network.side.server then serverUpdate(entityID, timeDelta, isActor)
	elseif lime.network.side.client then clientUpdate(entityID, timeDelta, isActor) end
end

function Lime_PostUpdate(entityID, timeDelta, isActor)
	if lime.network.side.server then serverPostUpdate(entityID, timeDelta, isActor)
	elseif lime.network.side.client then clientPostUpdate(entityID, timeDelta, isActor) end
end

function Lime_GetUnlocalizedName(entityID)
	return "lime.entity.ball.name"
end
