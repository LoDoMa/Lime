
local function follow(mask, body)
	lime.body.set(body)
	local translation = lime.body.translation.get()
	local rotation = lime.body.rotation.get()

	lime.mask.set(mask)
	lime.mask.translation.set(translation)
	lime.mask.rotation.set(rotation)
end

function Lime_FrameUpdate(timeDelta)
	lime.entity.set(lime.this.ID)
	follow("m_head", "head")
	follow("m_body", "body")
end
