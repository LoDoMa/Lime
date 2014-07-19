
local firstUpdate = true

local round = lime.util.round
local newVector = lime.util.vector.new

local function follow(mask, body)
	lime.body.set(body)
	local translation = lime.body.translation.get()
	local rotation = lime.body.rotation.get()

	lime.mask.set(mask)
	lime.mask.translation.set(translation)
	lime.mask.rotation.set(rotation)
end

function Lime_FrameUpdate(timeDelta)
	if firstUpdate then
		lime.entity.set(lime.this.ID)
		lime.body.set("head")
		lime.body.impulse.linear(newVector(100, 0), newVector(0, 0))
		firstUpdate = false
	end

	translation = lime.body.translation.get()
	print(round(translation.x, 2) .. " " .. round(translation.y, 2))
end
