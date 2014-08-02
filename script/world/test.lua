
local firstUpdate = true

local round = lime.util.round
local newVector = lime.util.vector.new
local hash32 = lime.util.hash32

function Lime_WorldServerUpdate()
	if firstUpdate == true then
		local vertices = {}
		vertices[1] = newVector(0, 0)
		vertices[2] = newVector(10, 0)
		vertices[3] = newVector(10, 2)
		vertices[4] = newVector(0, 2)

		lime.platform.create(newVector(0, 0), vertices)
		firstUpdate = false
	end
end

function Lime_WorldClientUpdate()
	
end

function Lime_WorldRender()

end