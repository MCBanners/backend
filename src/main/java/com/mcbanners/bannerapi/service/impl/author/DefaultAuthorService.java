package com.mcbanners.bannerapi.service.impl.author;

import com.mcbanners.bannerapi.obj.generic.Author;
import com.mcbanners.bannerapi.service.ServiceBackend;
import com.mcbanners.bannerapi.service.api.AuthorService;
import com.mcbanners.bannerapi.service.impl.author.backend.BuiltByBitAuthorService;
import com.mcbanners.bannerapi.service.impl.author.backend.CurseForgeAuthorService;
import com.mcbanners.bannerapi.service.impl.author.backend.ModrinthAuthorService;
import com.mcbanners.bannerapi.service.impl.author.backend.OreAuthorService;
import com.mcbanners.bannerapi.service.impl.author.backend.PolymartAuthorService;
import com.mcbanners.bannerapi.service.impl.author.backend.SpigotAuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = {"author"})
public class DefaultAuthorService implements AuthorService {
    private final SpigotAuthorService spigot;
    private final OreAuthorService ore;
    private final CurseForgeAuthorService curseForge;
    private final ModrinthAuthorService modrinth;
    private final PolymartAuthorService polymart;
    private final BuiltByBitAuthorService builtByBit;

    @Autowired
    public DefaultAuthorService(SpigotAuthorService spigot, OreAuthorService ore, CurseForgeAuthorService curseForge, ModrinthAuthorService modrinth, PolymartAuthorService polymart, BuiltByBitAuthorService builtByBit) {
        this.spigot = spigot;
        this.ore = ore;
        this.curseForge = curseForge;
        this.modrinth = modrinth;
        this.polymart = polymart;
        this.builtByBit = builtByBit;
    }

    /**
     * Get an author by its id on the specified service backend.
     *
     * @param authorId the author ID
     * @param backend  the service backend to query
     * @return the Author object or null if the service backend does not support the operation or the author could not be found.
     */
    @Override
    @Cacheable(unless = "#result == null")
    public Author getAuthor(int authorId, ServiceBackend backend) {
        switch (backend) {
            case SPIGOT:
                return spigot.handleSpigot(authorId);
            case CURSEFORGE:
                return curseForge.handleCurseForge(authorId, null);
            case BUILTBYBIT:
                return builtByBit.handleBuiltByBit(authorId);
            case POLYMART:
                return polymart.handlePolymart(authorId);
            case ORE:
            default:
                return null;
        }
    }

    @Override
    @Cacheable(unless = "#result == null")
    public Author getAuthor(int authorId, int resourceId, ServiceBackend backend) {
        return polymart.handlePolymart(authorId, resourceId);
    }

    /**
     * Get an author by its name on the specified service backend.
     *
     * @param authorName the author name
     * @param backend    the service backend to query
     * @return the Author object or null if the service bannerapi does not support the operation or the author could not be found.
     */
    @Override
    @Cacheable(unless = "#result == null")
    public Author getAuthor(String authorName, ServiceBackend backend) {
        switch (backend) {
            case ORE:
                return ore.handleOre(authorName);
            case CURSEFORGE:
                return curseForge.handleCurseForge(0, authorName);
            case MODRINTH:
                return modrinth.handleModrinth(authorName);
            case SPIGOT:
            case POLYMART:
            case BUILTBYBIT:
            default:
                return null;
        }
    }
}
