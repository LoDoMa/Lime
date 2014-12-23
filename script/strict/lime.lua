
local strict = {}
strict.script = LIME_SCRIPT

lime = {}

local function addTo(table, add)
	for k, v in pairs(add) do
		if table[k] then
			if type(v) == "table" and type(table[k] == "table") then
				addTo(table[k], v)
			end
		else
			table[k] = v
		end
	end
end

function addToStrict(table)
	addTo(strict, table)
end

function strictRequireJava(name)
	return luajava.bindClass(name)
end

function addToLime(table)
	addTo(lime, table)
end

function getStrict()
	return strict
end

local function checkLuaType(value, etype, argument, name)
	local etype_type = type(etype)
	local argument_type = type(argument)
	local name_type = type(name)

	assert(etype_type == "string", "invalid argument #2 to \"strict utility checkType\", expected string, got " .. etype_type)
	assert(argument_type == "number", "invalid argument #3 to \"strict utility checkType\", expected number, got " .. argument_type)
	assert(name_type == "string", "invalid argument #4 to \"strict utility checkType\", expected string, got " .. name_type)

	local gtype = type(value)
	assert(gtype == etype, "invalid argument #" .. argument .. " to \"" .. name .. "\", expected " .. etype .. ", got " .. gtype)
end

local HashHelper = strictRequireJava("net.lodoma.lime.util.HashHelper")

local function round(num, idp)
	strict.typecheck.lua(num, "number", 1, "lime.util.round")
	strict.typecheck.lua(idp, "number", 2, "lime.util.round")
	local mult = 10 ^ (idp or 0)
	return math.floor(num * mult + 0.5) / mult
end

local function hash32(str)
	strict.typecheck.lua(str, "string", 1, "lime.util.hash32")
	return HashHelper:hash32(str)
end

local function hash64(str)
	strict.typecheck.lua(str, "string", 1, "lime.util.hash64")
	return HashHelper:hash64(str)
end

addToStrict({
	typecheck = {
		lua = checkLuaType,
	},
	util = {
		hash32 = hash32,
		hash64 = hash64,
	}
})

addToLime({
	util = {
		round = round,
		hash32 = hash32,
		hash64 = hash64,
	},
})