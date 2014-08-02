
local firstUpdate = true

local round = lime.util.round
local newVector = lime.util.vector.new
local hash32 = lime.util.hash32

local hashes = {}

local function addHash(str)
	hashes[str] = hash32(str)
end

local function loadHashes()
	addHash("Lime::Zombie")
	addHash("Lime::OnNewUser")
end

local function onNewUser(bundle)
	print("user ID: " .. bundle["userID"])

	for i = 0, 50, 1 do
		lime.entity.create(hashes["Lime::Zombie"])
	end

	print("created all")
end

function Lime_WorldUpdate()
	if lime.network.side.server then
		if firstUpdate == true then
			loadHashes()

			lime.platform.create(newVector(6, 0), newVector(-5, -2), newVector(5, -2), newVector(5, 2), newVector(-5, 2))
			firstUpdate = false

			lime.listener.set(hashes["Lime::OnNewUser"], onNewUser)
		end
	elseif lime.network.side.client then

	end
end

function Lime_WorldRender()

end