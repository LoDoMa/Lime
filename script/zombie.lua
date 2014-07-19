local firstUpdate = true

function Lime_FrameUpdate(timeDelta)
	if firstUpdate then
		Lime.addListener("Lime::eventCollision", eventCollision)
		firstUpdate = false
	end
end

local function eventCollision(eventData)
	thisBody = eventData:getThisBody()
	otherBody = eventData:getOtherBody()
end