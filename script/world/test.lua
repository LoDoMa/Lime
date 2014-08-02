
local firstUpdate = true

local round = lime.util.round
local newVector = lime.util.vector.new
local hash32 = lime.util.hash32

function Lime_WorldServerUpdate()
	if firstUpdate == true then
		lime.platform.create(newVector(0, 0), newVector(0, 0), newVector(10, 0), newVector(10, 2), newVector(0, 2))
		firstUpdate = false
	end
end

function Lime_WorldClientUpdate()
	
end

function Lime_WorldRender()

end