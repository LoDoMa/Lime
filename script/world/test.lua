
local firstUpdate = true

function Lime_Update(timeDelta)
	if firstUpdate == true then
		local hash = lime.hash32("lime.entity.ball")
		
		for i = 1, 1000 do
			local id = lime.newEntity(hash)
			local posx, posy = (math.random() - 0.0) * 40, (math.random() - 0.0) * 35
			local velx, vely = (math.random() - 0.5) * 2, (math.random() - 0.5) * 2
			local radius = 0.25 + math.random() * 0.5
			local density = 1.0

			lime.setEntity(id)
			lime.setBodyPosition(posx, posy)
			lime.setBodyVelocity(velx, vely)
			lime.setBodyRadius(radius)
			lime.setBodyDensity(density)
		end

		firstUpdate = false
	end
end
