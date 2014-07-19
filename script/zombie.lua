
local firstUpdate = true

local this = lime.this.ID
local round = lime.util.round
local newVector = lime.util.vector.new

function Lime_FrameUpdate(timeDelta)
	if firstUpdate then
		lime.entity.set(this)
		lime.body.set("head")
		lime.body.impulse.linear(newVector(100, 0), newVector(0, 0))
		firstUpdate = false
	end

	local translation = lime.body.translation.get()
	print(round(translation.x, 2) .. " " .. round(translation.y, 2))

	limex.follow(this, "head", this, "m_head")
	limex.follow(this, "body", this, "m_body")
end
