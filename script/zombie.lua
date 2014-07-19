
local this = lime.this
local entity = lime.entity
local body = lime.body
local joint = lime.joint
local mask = lime.mask
local property = lime.property
local listener = lime.listener
local util = lime.util

function Lime_FrameUpdate(timeDelta)
	entity.set(this.ID)

	body.set("head")
	local headTranslation = body.translation.get()
	local headRotation = body.rotation.get()

	mask.set("m_head")
	mask.translation.set(headTranslation)
	mask.rotation.set(headRotation)

	body.set("body")
	local bodyTranslation = body.translation.get()
	local bodyRotation = body.rotation.get()

	mask.set("m_body")
	mask.translation.set(bodyTranslation)
	mask.rotation.set(bodyRotation)
end
