
local firstUpdate = true

function Lime_Update(timeDelta)
	if firstUpdate == true then
		lime.entity.create(642)
	end

	firstUpdate = false
end
