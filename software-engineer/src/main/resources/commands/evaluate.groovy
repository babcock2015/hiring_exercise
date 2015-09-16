package commands;

// import org.crsh.cmdline.annotations.Argument
// import org.crsh.cmdline.annotations.Command
// import org.crsh.cmdline.annotations.Usage
import org.crsh.shell.impl.command.CRaSHSession
import org.crsh.command.GroovyScriptCommand

public class evaluate extends org.crsh.command.CRaSHCommand {

    @Command
    @Usage("evaluate groovy script")
    public void main(@Usage("the code") @Argument String scriptText) {
        CRaSHSession session = (CRaSHSession)context.session;
        GroovyShell shell = session.getGroovyShell();
        GroovyScriptCommand script = shell.parse(scriptText);
        script.setSession(session);
        script.open(context);
        script.flush()
        script.close();
    }
}

