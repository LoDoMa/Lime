
local firstUpdate = true

function Lime_WorldInit()
	lime.setWorldGravity(0.0, 0.0)
end

function Lime_Update(timeDelta)
	if firstUpdate == true then
		for i = 1, 50 do
			local id = lime.newEntity()
			lime.assignScript(id, "ball")
		end

		firstUpdate = false
	end
end
