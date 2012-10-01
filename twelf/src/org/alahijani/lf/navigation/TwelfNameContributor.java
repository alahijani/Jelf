package org.alahijani.lf.navigation;

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.util.ArrayUtil;
import org.alahijani.lf.psi.stubs.index.LfGlobalVariableIndex;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Ali Lahijani
 */
public class TwelfNameContributor implements ChooseByNameContributor {
    @NotNull
    public String[] getNames(Project project, boolean includeNonProjectItems) {
        Set<String> symbols = new HashSet<String>();
        symbols.addAll(StubIndex.getInstance().getAllKeys(LfGlobalVariableIndex.KEY, project));
        return ArrayUtil.toStringArray(symbols);
    }

    @NotNull
    public NavigationItem[] getItemsByName(String name, String pattern, Project project, boolean includeNonProjectItems) {
        GlobalSearchScope scope = includeNonProjectItems ? GlobalSearchScope.allScope(project) : GlobalSearchScope.projectScope(project);

        List<NavigationItem> symbols = new ArrayList<NavigationItem>();
        symbols.addAll(StubIndex.getInstance().get(LfGlobalVariableIndex.KEY, name, project, scope));

        return symbols.toArray(new NavigationItem[symbols.size()]);
    }
}
