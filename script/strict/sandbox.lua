
local whitelist = {
	_G = true,
	lime = true,
	limex = true,

	assert = true,
	error = true,
	ipairs = true,
	next = true,
	pairs = true,
	pcall = true,
	print = true, -- stdout is fine
	select = true,
	tonumber = true,
	tostring = true,
	type = true,
	unpack = true,
	xpcall = true,
	coroutine = {
		create = true,
		resume = true,
		running = true,
		status = true,
		wrap = true,
		yield = true, -- handled by scripts
	},
	string = {
		byte = true,
		char = true,
		find = true, -- CPU usage
		format = true,
		gmatch = true,
		gsub = true,
		len = true,
		lower = true,
		match = true,
		rep = true,
		reverse = true,
		sub = true,
		upper = true,
	},
	table = {
		insert = true,
		maxn = true,
		remove = true,
		sort = true,
	},
	math = {
		abs = true,
		acos = true,
		asin = true,
		atan = true,
		atan2 = true,
		ceil = true,
		cos = true,
		cosh = true,
		deg = true,
		exp = true,
		floor = true,
		fmod = true,
		frexp = true,
		huge = true,
		ldexp = true,
		log = true,
		log10 = true,
		max = true,
		min = true,
		modf = true,
		pi = true,
		pow = true,
		rad = true,
		random = true,
		sin = true,
		sinh = true,
		sqrt = true,
		tan = true,
		tanh = true,
	},
}

local function removeElements(t1, t2)
	for k, v in pairs(t1) do
		if(t1[k] ~= nil) then
			if(t2[k] == nil) then
				t1[k] = nil
			elseif type(t2[k]) == "table" then
				removeElements(v, t2[k])
			end
		end
	end
end

removeElements(_G, whitelist)
