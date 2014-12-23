
local firstUpdate = true

function Lime_Update(timeDelta)
	if firstUpdate == true then
		local hash = lime.hash32("lime.entity.ball")
		
		local id = lime.newEntity(hash)
		print(id)

		firstUpdate = false
	end
end
