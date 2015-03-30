
lime.include("WorldUtils")
lime.include("Material")

-- Constants
local cameraRatioX = 16
local cameraRatioY = 9
local cameraRatio = cameraRatioX / cameraRatioY
local cameraPadding = 4
local minCameraWidth = 32
local minCameraHeight = 18

local connectedUsers = {}
local playerIDs = {}

function Lime_WorldInit()
	lime.setWorldGravity(0.0, -10.0)
end

local function createPlayer(userID)
	local playerID = lime.newEntity()
	lime.setAttribute(playerID, "owner", userID)
	lime.assignScript(playerID, "Player")
	return playerID
end

local function onJoin(userID)
	local playerID = createPlayer(userID)
	connectedUsers[userID] = playerID
end

local function onLeave(userID)
	local playerID = connectedUsers[userID]
	connectedUsers[userID] = nil

	lime.removeEntity(playerID)
end

function Lime_Init()
	lime.addEventListener("Lime::OnJoin", onJoin)
	lime.addEventListener("Lime::OnLeave", onLeave)

	-- TODO: remove crazy ambient light, add normal lights
	lime.setAmbientLight(1, 1, 1)

	addMaterial("stone", 5, 0.6, 0.1)

	addTerrain("stone", 0, 0, 0, -2, 20, -2) -- bottom
	addTerrain("stone", 0, 0, 20, -2, 20, 0) -- bottom
	addTerrain("stone", -2, 0, 0, -2, 0, 0) -- bottom left corner
	addTerrain("stone", 20, 0, 20, -2, 22, 0) -- bottom right corner
	addTerrain("stone", -2, 9.5, -2, 0, 0, 0) -- left
	addTerrain("stone", -2, 9.5, 0, 0, 0, 9.5) -- left
	addTerrain("stone", 20, 9.5, 20, 0, 22, 0) -- right
	addTerrain("stone", 20, 9.5, 22, 0, 22, 9.5) -- right
	addTerrain("stone", 0, 11.5, 0, 9.5, 20, 9.5) -- top
	addTerrain("stone", 0, 11.5, 20, 9.5, 20, 11.5) -- top
	addTerrain("stone", -2, 9.5, 0, 9.5, 0, 11.5) -- top left corner
	addTerrain("stone", 20, 11.5, 20, 9.5, 22, 9.5) -- top right corner

	addTerrain("stone", 3, 4, 3, 3, 9.5, 3) -- obstacle bottom-left
	addTerrain("stone", 3, 4, 9.5, 3, 9.5, 4) -- obstacle bottom-left
	addTerrain("stone", 10.5, 4, 10.5, 3, 17, 3) -- obstacle bottom-right
	addTerrain("stone", 10.5, 4, 17, 3, 17, 4) -- obstacle bottom-right
	addTerrain("stone", 9.5, 6.5, 9.5, 3, 10.5, 3) -- obstacle middle
	addTerrain("stone", 9.5, 6.5, 10.5, 3, 10.5, 6.5) -- obstacle middle
	addTerrain("stone", 3, 6.5, 3, 5.5, 9.5, 5.5) -- obstacle top-left
	addTerrain("stone", 3, 6.5, 9.5, 5.5, 9.5, 6.5) -- obstacle top-left
	addTerrain("stone", 10.5, 6.5, 10.5, 5.5, 17, 5.5) -- obstacle top-right
	addTerrain("stone", 10.5, 6.5, 17, 5.5, 17, 6.5) -- obstacle top-right


	print("Gamemode initialized")
end

function Lime_Update(timeDelta)

end

function Lime_PostUpdate()
	local cameraBoundsMinX = nil
	local cameraBoundsMaxX = nil
	local cameraBoundsMinY = nil
	local cameraBoundsMaxY = nil

	for user, playerID in pairs(connectedUsers) do
		local focusX = lime.getAttribute(playerID, "focusX")
		local focusY = lime.getAttribute(playerID, "focusY")

		if cameraBoundsMinX then
			cameraBoundsMinX = math.min(cameraBoundsMinX, focusX)
			cameraBoundsMaxX = math.max(cameraBoundsMaxX, focusX)
			cameraBoundsMinY = math.min(cameraBoundsMinY, focusY)
			cameraBoundsMaxY = math.max(cameraBoundsMaxY, focusY)
		else
			cameraBoundsMinX = focusX
			cameraBoundsMaxX = focusX
			cameraBoundsMinY = focusY
			cameraBoundsMaxY = focusY
		end
	end

	if cameraBoundsMinX then
		cameraBoundsMinX = cameraBoundsMinX - cameraPadding
		cameraBoundsMaxX = cameraBoundsMaxX + cameraPadding
		cameraBoundsMinY = cameraBoundsMinY - cameraPadding
		cameraBoundsMaxY = cameraBoundsMaxY + cameraPadding

		local cameraBoundsWidth = cameraBoundsMaxX - cameraBoundsMinX
		local cameraBoundsHeight = cameraBoundsMaxY - cameraBoundsMinY

		local cameraCenterX = cameraBoundsMaxX - cameraBoundsWidth / 2.0
		local cameraCenterY = cameraBoundsMaxY - cameraBoundsHeight / 2.0

		if cameraBoundsWidth < minCameraWidth then cameraBoundsWidth = minCameraWidth end
		if cameraBoundsHeight < minCameraHeight then cameraBoundsHeight = minCameraHeight end

		local currentRatio = cameraBoundsWidth / cameraBoundsHeight
		if currentRatio > cameraRatio then
			cameraBoundsHeight = cameraBoundsWidth / cameraRatio
		elseif currentRatio < cameraRatio then
			cameraBoundsWidth = cameraBoundsHeight * cameraRatio
		end

		local cameraTranslationX = cameraCenterX - cameraBoundsWidth / 2.0
		local cameraTranslationY = cameraCenterY - cameraBoundsHeight / 2.0

		for userID in pairs(connectedUsers) do
			lime.setCameraTranslation(userID, cameraTranslationX, cameraTranslationY)
			lime.setCameraScale(userID, cameraBoundsWidth, cameraBoundsHeight)
		end
	end
end

function Lime_Clean()

end
