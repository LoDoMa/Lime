
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
	lime.setWorldGravity(0.0, 0.0)
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
