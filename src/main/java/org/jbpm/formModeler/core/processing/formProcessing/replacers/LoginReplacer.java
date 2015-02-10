package org.jbpm.formModeler.core.processing.formProcessing.replacers;


import javax.enterprise.context.ApplicationScoped;
import org.apache.commons.lang.StringUtils;
import org.jbpm.formModeler.core.processing.formProcessing.replacers.FormulaReplacer;
import org.uberfire.security.Identity;
import javax.inject.Inject;

/**
 * Replaces the {$login} token on formula.
 */
@ApplicationScoped
public class LoginReplacer implements FormulaReplacer {
	
    @Inject
    private Identity identity;
	public static final String THIS_TOKEN = "{$login}";
    public String replace(FormulaReplacementContext ctx) {
    	
    	if (ctx.isBeforeFieldEvaluation()) {
            if (isFormulaSurroundedByQuotes(ctx.getFormula()))
                return StringUtils.replace(ctx.getFormula(), THIS_TOKEN, identity.getName());
            return StringUtils.replace(ctx.getFormula(), THIS_TOKEN, "\"" + identity.getName() + "\"");
        } else {
            return StringUtils.replace(ctx.getFormula(), THIS_TOKEN, identity.getName());
        }
    }

    protected boolean isFormulaSurroundedByQuotes(String formula) {
        if (StringUtils.isEmpty(formula) || !formula.contains(THIS_TOKEN)) return false;
        return hasOpenQuote(formula) && hasCloseQuote(formula);
    }

    protected boolean hasOpenQuote(String formula) {
        int count = countQuotes(formula.substring(0, formula.indexOf(THIS_TOKEN)));
        return count % 2 != 0;
    }

    protected boolean hasCloseQuote(String formula) {
        int count = countQuotes(formula.substring(formula.indexOf(THIS_TOKEN) + 1));
        return count % 2 != 0;
    }

    private int countQuotes(String formula) {
        int count = 0;
        for (int i = 0; i < formula.length(); i++) {
            if (formula.charAt(i) == '\"') count++;
        }
        return count;
    }
}
