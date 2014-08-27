
local strict = getStrict()
strictRequireJava("net.lodoma.lime.common.NetworkSide")

local networkSide = strict.entityWorld:getNetworkSide():ordinal()
local serverSide = networkSide == strict.java["net.lodoma.lime.common.NetworkSide"]:valueOf("SERVER"):ordinal() and true or nil
local clientSide = networkSide == strict.java["net.lodoma.lime.common.NetworkSide"]:valueOf("CLIENT"):ordinal() and true or nil

local packetHashPool = strict.propertyPool:getProperty(serverSide and "spPool" or (clientSide and "cpPool" or nil))
local packetHandlerHashPool = strict.propertyPool:getProperty(serverSide and "sphPool" or (clientSide and "cphPool" or nil))

local packetCache = {}
local packetHandlerCache = {}

local function getPacket(hash)
	strict.typecheck.lua(hash, "number", 1, "local utility getPacket")
	if packetCache[hash] then
		return packetCache[hash]
	end
	local packet = packetHashPool:get(hash)
	packetCache[hash] = packet
	return packet
end

local function getPacketHandler(hash)
	strict.typecheck.lua(hash, "number", 1, "local utility getPacketHandler")
	if packetHandlerCache[hash] then
		return packetHandlersCache[hash]
	end
	local packetHandler = packetHandlerHashPool:get(hash)
	packetHandlerCache[hash] = packetHandler
	return packetHandler
end

addToStrict({
	network = {
		getPacket = getPacket,
		getPacketHandler = getPacketHandler,
	},
})

addToLime({
	network = {
		side = {
			server = serverSide,
			client = clientSide,
		},
	},
})