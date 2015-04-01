
--[[

bodyc: 1-8
size[i(1-bodyc)]: (0.1-1, 0.1-1)
parent[i(2-bodyc)]: 1-bodyc != i
anchor[i(2-bodyc)]%

stren[i(2-bodyc)]: (0-1) (0-1)
timing[i(2-bodyc)]: (0-10) (0-10) (0-10)

]]

local MAX_SIZE_DIF = 0.1

function genome_initial()
    local genome = {}
    genome.bodyc = 2

    genome.size = {}
    genome.parent = {}
    genome.anchor = {}
    genome.stren = {}
    genome.timing = {}

    genome.size[1] = {x = 0.5, y = 0.5}

    genome.size[2] = {x = 0.5, y = 0.5}
    genome.parent[2] = 1
    genome.anchor[2] = {x1 = 1.0, y1 = 1.0, x2 = 0.0, y2 = 0.0}
    genome.stren[2] = { p = 8.0, r = -8.0, w = 0.0 }
    genome.timing[2] = { p = 1.0, r = 1.0, w = 0.5 }

    return genome
end

function genome_print(genome)
    print("Genome:")
    print("  bodyc: " .. genome.bodyc)
    for i = 1, genome.bodyc do
        print("  body #" .. i)
        print("    size: (" .. genome.size[i].x .. ", " .. genome.size[i].y .. ")")
        if i > 1 then
            print("    parent: " .. genome.parent[i])
            print("    anchor: A(" .. genome.anchor[i].x1 .. ", " .. genome.anchor[i].y1 .. ") B(" .. genome.anchor[i].x2 .. ", " .. genome.anchor[i].y2 .. ")")
            print("    stren: p" .. genome.stren[i].p .. " r" .. genome.stren[i].r)
            print("    timing: p" .. genome.timing[i].p .. " r" .. genome.timing[i].r .. " w" .. genome.timing[i].w)
        end
    end
end

function genome_duplicate(genome)

end

local function randint(from, to) return math.random(from, to) end
local function randfloatn() return (math.random() - 0.5) * 2 end
local function randfloat() return math.random() end

function genome_modify(genome)
    local rand = randfloat()
    local i = randint(1, genome.bodyc)

    if rand < 0.05 then
        -- modifying bodyc
        local dir = randfloat()
        if dir < 0.2 and i > 1 then
            print("removing a body component")
            genome.bodyc = genome.bodyc - 1
        elseif dir >= 0.2 or i == 1 then
            print("adding new component")
            genome.bodyc = genome.bodyc + 1

            genome.size[genome.bodyc] = {x = 0.5, y = 0.5}
            genome.parent[genome.bodyc] = randint(1, genome.bodyc - 1)
            genome.anchor[genome.bodyc] = {x1 = 1.0, y1 = 1.0, x2 = 0.0, y2 = 0.0}
            genome.stren[genome.bodyc] = { p = 8.0, r = -8.0, w = 0.0 }
            genome.timing[genome.bodyc] = { p = 1.0, r = 1.0, w = 0.5 }
        end
    elseif rand < 0.2 then
        -- modifying size
        print("modifying size")
        local xc, yc = randfloatn() * MAX_SIZE_DIF, randfloatn() * MAX_SIZE_DIF

        genome.size[i].x = genome.size[i].x + xc
        genome.size[i].y = genome.size[i].y + yc
        if genome.size[i].x < 0.1 then genome.size[i].x = 0.1 end
        if genome.size[i].x > 1.0 then genome.size[i].x = 1.0 end
        if genome.size[i].y < 0.1 then genome.size[i].y = 0.1 end
        if genome.size[i].y > 1.0 then genome.size[i].y = 1.0 end
    elseif i > 1 then
        if rand < 0.35 then
            -- modifying parent
            print("modifying parent")
            local pi = randint(1, genome.bodyc - 1)
            if pi >= i then pi = pi + 1 end

            genome.parent[i] = pi
        end
    end
end

