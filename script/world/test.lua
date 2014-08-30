
local firstUpdate = true

local round = lime.util.round
local newVector = lime.util.vector.new
local newColor = lime.util.color.new
local hash32 = lime.util.hash32

local hashes = {}

local function addHash(str)
	hashes[str] = hash32(str)
end

local function loadHashes()
	addHash("lime.entity.zombie")
	addHash("lime.entity.ball")
	addHash("Lime::OnNewUser")
end

local function onNewUser(bundle)
	if lime.network.side.client then
		print("adding light")
		lime.light.basic.add(0, newVector(16, 15), 40, newColor(1, 1, 1, 1), -1, 361)
	elseif lime.network.side.server then
		print("user ID: " .. bundle["userID"])

		--local id = lime.entity.create(hashes["lime.entity.zombie"])
		--lime.actor.set(id, bundle["userID"])
		for i = 0, 10, 1 do
			lime.entity.create(hashes["lime.entity.ball"])
		end

		print("created all")
	end
end

function Lime_WorldUpdate()
	if lime.network.side.server then
		if firstUpdate == true then
			loadHashes()
		end
	elseif lime.network.side.client then

	end

	if firstUpdate == true then
		lime.listener.set(hashes["Lime::OnNewUser"], onNewUser)
	end

	firstUpdate = false
end

function Lime_WorldRender()

end