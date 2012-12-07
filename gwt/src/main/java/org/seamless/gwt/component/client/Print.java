/**
 * https://groups.google.com/d/msg/Google-Web-Toolkit/7qSLr76O7WM/T-uC7SYLb2EJ
 *
 * <pre>
 *
 * Description:
 *
 *	Generic printing class
 *	Can be used to print the Window it self, DOM.Elements, UIObjects (Widgets) and plain HTML
 *
 * Usage:
 *
 *	You must insert this iframe in your host page:
 *		<iframe id="__printingFrame" style="width:0;height:0;border:0"></iframe>
 *
 *	Window:
 *		Print.it();
 *
 *	Objects/HTML:
 *		Print.it(RootPanel.get("myId"));
 *		Print.it(DOM.getElementById("myId"));
 *		Print.it("Just <b>Print.it()</b>!");
 *
 *	Objects/HTML using styles:
 *		Print.it("<link rel=StyleSheet type=text/css media=paper href=/paperStyle.css>", RootPanel.get("myId"));
 *		Print.it("<style type=text/css media=paper> .newPage { page-break-after: always; } </style>",
 *				"Hi<p class=newPage></p>By");
 *
 *	Objects/HTML using styles and DocType:
 *		Print.it("<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01//EN' 'http://www.w3.org/TR/html4/strict.dtd'>",
 *                       "<link rel=StyleSheet type=text/css media=paper href=/paperStyle.css>",
 *                       RootPanel.get("myId"));
 *
 * OBS:
 *
 *	Warning: You can't use \" in your style String
 *
 *	Obs: If your machine is to slow to render the page and you keep getting blank pages, change USE_TIMER to true and
 *	     play with TIMER_DELAY
 *
 *	Obs: If you try to print Form elements, like TextArea and ListBox they will show default status
 *
 * </pre>
 */
package org.seamless.gwt.component.client;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.Timer;

public class Print {

    /**
     * If true, use a Timer instead of DeferredCommand to print the internal fram
     */
    public static boolean USE_TIMER = false;

    /**
     * Time in seconds to wait before printing the internal frame when using Timer
     */
    public static int TIMER_DELAY = 2;


    public static native void it() /*-{
        $wnd.print();
    }-*/;

    public static void it(UIObject obj) {
        it("", obj);
    }

    public static void it(Element element) {
        it("", element);
    }

    public static void it(String style, UIObject obj) {
        it(style, obj.getElement());
    }

    public static void it(String style, Element element) {
        it("", style, element);
    }

    public static void it(String docType, String style, Element element) {
        // TODO DOM.toString
        it(docType, style, DOM.toString(element));
    }

    public static void it(String docType, String style, String it) {
        it(docType
                   + "<html>"
                   + "<head>"
                   + "<meta http-equiv=\"Content-Type\"		content=\"text/html; charset=utf-8\">"
                   + "<meta http-equiv=\"Content-Style-Type\"	content=\"text/css\">"
                   + style
                   + "</head>" + "<body>"
                   + it
                   + "</body>" +
                   "</html>");
    }

    public static void it(String html) {
        try {
            buildFrame(html);

            if (USE_TIMER) {
                Timer timer = new Timer() {
                    public void run() {
                        printFrame();
                    }
                };
                timer.schedule(TIMER_DELAY * 1000);
            } else {
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                        printFrame();
                    }
                });
            }

        } catch (Throwable exc) {
            Window.alert(exc.getMessage());
        }
    }

    public static native void buildFrame(String html) /*-{
        var frame = $doc.getElementById('__printingFrame');
        if (!frame) {
            $wnd.alert("Error: Can't find printing frame.");
            return;
        }
        var doc = frame.contentWindow.document;
        doc.open();
        doc.write(html);
        doc.close();

    }-*/;

    public static native void printFrame() /*-{
        var frame = $doc.getElementById('__printingFrame');
        frame = frame.contentWindow;
        frame.focus();
        frame.print();
    }-*/;

} // end of class Print
