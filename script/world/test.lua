
local firstUpdate = true

function Lime_Update(timeDelta)
	if firstUpdate == true then
		lime.entity.create(lime.util.hash32("lime.entity.ball"))

		firstUpdate = false
	end
end
