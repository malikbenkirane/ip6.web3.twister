package edu.twister.malik.services;

import java.util.Hashtable;
import java.util.Enumeration;

public class ServletMap {

    private Hashtable<String, String> specs;
    private Hashtable<String, String[]> inputs = null;

    public ServletMap
        (Hashtable<String, String> specs) {
            this.specs = specs;
        }

    public void setInputs
        (Hashtable<String, String[]> inputs) throws ServletException {
            Enumeration<String> specification = specs.keys();
            Hashtable<String, String> errors = 
                new Hashtable<String, String>();
            for ( ; specification.hasMoreElements() ; ) {
                String spec = specification.nextElement();
                if ( inputs.containsKey(spec) )
                    if ( inputs.get(spec).length > 0 )
                        continue;
                    else
                        errors.put(spec, specs.get(spec));
                else errors.put(spec, "missing");
            }
            if ( errors.size() > 0 )
                throw new ServletException(errors);
            this.inputs = inputs;
        }

    public String 
        getNotSafe(String parameter) throws ServletException {
            if ( inputs == null )
                throw new ServletException("no inputs");
            if ( inputs.containsKey(parameter) )
                return inputs.get(parameter)[0];
            throw new ServletException("not safe");
        }

}
