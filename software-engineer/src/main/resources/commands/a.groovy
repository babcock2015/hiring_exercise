package commands;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.Map
//import java.text.BreakIterator;
//boundry = BreakIterator.getWordInstance();
//boundry.setText(bodyStrings[0]);



def mapper = new ObjectMapper();
def tree = mapper.readTree(new URL("https://www.reddit.com/r/java/comments/32pj67/java_reference_in_gta_v_beautiful/.json"));
bodyStrings = tree.findValuesAsText('body')

words = bodyStrings[0].replace("'", "").split("\\W+")
lowerCaseWords = words.toList().stream().map({s -> s.toLowerCase()}).collect()



import org.crsh.cli.Usage
import org.crsh.cli.Command

class hello {

    @Usage("Say Hello")
    @Command
    def main(InvocationContext context) {
		def mapper = new ObjectMapper();
		def tree = mapper.readTree(new URL("https://www.reddit.com/r/java/comments/32pj67/java_reference_in_gta_v_beautiful/.json"));
		return tree.metaClass.methods.name
    }

}



bodyStrings.asList().stream().flatMap({comment -> 
    return comment.replace("'", "").split("\\W+")
			.toList()
			.stream()
			.map({s -> s.toLowerCase()});
}).collect();


Map<String, Integer> counts  = bodyStrings.asList().stream().flatMap({comment -> 
    return comment.replace("'", "").split("\\W+")
			.toList()
			.stream()
			.map({s -> s.toLowerCase()});
}).collect(Collectors.groupingBy({o -> o}, Collectors.counting()));
