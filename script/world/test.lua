
local firstUpdate = true

function Lime_Update(timeDelta)
	if firstUpdate == true then
		local hash = lime.hash32("lime.entity.ball")
		
		for i = 1, 30 do
			local id = lime.newEntity(hash)
		end

		firstUpdate = false
	end
end
