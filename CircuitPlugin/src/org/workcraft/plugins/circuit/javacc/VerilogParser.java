/* Generated By:JavaCC: Do not edit this line. VerilogParser.java */
package org.workcraft.plugins.circuit.javacc;

import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;

import org.workcraft.plugins.circuit.verilog.Module;
import org.workcraft.plugins.circuit.verilog.Port;
import org.workcraft.plugins.circuit.verilog.Assign;
import org.workcraft.plugins.circuit.verilog.Instance;
import org.workcraft.plugins.circuit.verilog.Pin;

public class VerilogParser implements VerilogParserConstants {

  final public List<Module> parseCircuit() throws ParseException {
    List<Module> modules;
    modules = parseModules();
        {if (true) return modules;}
    throw new Error("Missing return statement in function");
  }

  final public List<Module> parseModules() throws ParseException {
    Module module;
    List<Module> modules = new LinkedList<Module>();
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case MODULE:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
      module = parseModule();
            modules.add(module);
    }
        {if (true) return modules;}
    throw new Error("Missing return statement in function");
  }

  final public Module parseModule() throws ParseException {
    String name;
    List<Port> ports;
    List<Assign> assigns;
    List<Instance> instances;
    List<Instance> group;
    Set<List<Instance>> groups = new HashSet<List<Instance>>();
    Set<String> highSignals = null;
    jj_consume_token(MODULE);
    name = parseModuleName();
    ports = parsePorts();
    assigns = parseAssigns();
    instances = parseInstances();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case PETRIFY_UNMAPPED:
      jj_consume_token(PETRIFY_UNMAPPED);
      label_2:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case PETRIFY_EQUATION:
          ;
          break;
        default:
          jj_la1[1] = jj_gen;
          break label_2;
        }
        jj_consume_token(PETRIFY_EQUATION);
        group = parseInstances();
                        instances.addAll(group);
                        groups.add(group);
      }
      break;
    default:
      jj_la1[2] = jj_gen;
      ;
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case PETRIFY_INIT_STATE:
      jj_consume_token(PETRIFY_INIT_STATE);
      highSignals = parseInitialState();
      break;
    default:
      jj_la1[3] = jj_gen;
      ;
    }
    jj_consume_token(ENDMODULE);
        {if (true) return new Module(name, ports, assigns, instances, highSignals, groups);}
    throw new Error("Missing return statement in function");
  }

  final public String parseModuleName() throws ParseException {
    Token nameToken;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NAME:
      nameToken = jj_consume_token(NAME);
      break;
    case PETRIFY_NAME:
      nameToken = jj_consume_token(PETRIFY_NAME);
      break;
    default:
      jj_la1[4] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
        {if (true) return nameToken.image;}
    throw new Error("Missing return statement in function");
  }

  final public List<Port> parsePorts() throws ParseException {
    List<Port> ports;
    if (jj_2_1(2147483647)) {
      ports = parseCompactPorts();
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 33:
      case 39:
        ports = parseComplexPorts();
        break;
      default:
        jj_la1[5] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
        {if (true) return ports;}
    throw new Error("Missing return statement in function");
  }

  final public List<Port> parseCompactPorts() throws ParseException {
    Port port;
    List<Port> ports = new LinkedList<Port>();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 33:
      jj_consume_token(33);
      label_3:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case INPUT:
        case OUTPUT:
          ;
          break;
        default:
          jj_la1[6] = jj_gen;
          break label_3;
        }
        port = parseCompactPort();
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 37:
          jj_consume_token(37);
          break;
        default:
          jj_la1[7] = jj_gen;
          ;
        }
                        ports.add(port);
      }
      jj_consume_token(34);
      break;
    default:
      jj_la1[8] = jj_gen;
      ;
    }
    jj_consume_token(39);
        {if (true) return ports;}
    throw new Error("Missing return statement in function");
  }

  final public Port parseCompactPort() throws ParseException {
    Port.Type type;
    Token nameToken;
    type = parsePortType();
    nameToken = jj_consume_token(NAME);
        {if (true) return new Port(nameToken.image, type);}
    throw new Error("Missing return statement in function");
  }

  final public Port.Type parsePortType() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INPUT:
      jj_consume_token(INPUT);
                {if (true) return Port.Type.INPUT;}
      break;
    case OUTPUT:
      jj_consume_token(OUTPUT);
                {if (true) return Port.Type.OUTPUT;}
      break;
    default:
      jj_la1[9] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public List<Port> parseComplexPorts() throws ParseException {
    List<String> declarations;
    List<Port> definitions;
    declarations = parsePortsDeclaration();
    definitions = parsePortsDefinitions();
        HashMap<String, Port.Type> nameToType = new HashMap<String, Port.Type>();
        for (Port port: definitions) {
                nameToType.put(port.name, port.type);
        }
        List<Port> ports = new LinkedList<Port>();
                for (String name: declarations) {
                        Port.Type type = nameToType.get(name);
                ports.add(new Port(name, type));
        }
        {if (true) return ports;}
    throw new Error("Missing return statement in function");
  }

  final public List<String> parsePortsDeclaration() throws ParseException {
    List<String> names = null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 33:
      jj_consume_token(33);
      names = parseNames();
      jj_consume_token(34);
      break;
    default:
      jj_la1[10] = jj_gen;
      ;
    }
    jj_consume_token(39);
                List<String> ports = new LinkedList<String>();
                if (names != null) {
                        ports.addAll(names);
                }
                {if (true) return ports;}
    throw new Error("Missing return statement in function");
  }

  final public List<Port> parsePortsDefinitions() throws ParseException {
    List<Port> ports;
    List<Port> allPorts = new LinkedList<Port>();
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case INPUT:
      case OUTPUT:
      case INOUT:
      case REG:
      case WIRE:
        ;
        break;
      default:
        jj_la1[11] = jj_gen;
        break label_4;
      }
      ports = parsePortsDefinition();
                if (ports != null) {
                        allPorts.addAll(ports);
                }
    }
        {if (true) return allPorts;}
    throw new Error("Missing return statement in function");
  }

  final public List<Port> parsePortsDefinition() throws ParseException {
    List<String> names;
    Port.Type type;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INPUT:
    case OUTPUT:
      type = parsePortType();
      names = parseNames();
      jj_consume_token(39);
                List<Port> ports = new LinkedList<Port>();
                for (String name: names) {
                        Port port = new Port(name, type);
                        ports.add(port);
                }
                {if (true) return ports;}
      break;
    case INOUT:
    case REG:
    case WIRE:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case WIRE:
        jj_consume_token(WIRE);
        break;
      case INOUT:
        jj_consume_token(INOUT);
        break;
      case REG:
        jj_consume_token(REG);
        break;
      default:
        jj_la1[12] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      parseNames();
      jj_consume_token(39);
                        {if (true) return null;}
      break;
    default:
      jj_la1[13] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public List<String> parseNames() throws ParseException {
    Token nameToken;
    List<String> names = new LinkedList<String>();
    label_5:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NAME:
        ;
        break;
      default:
        jj_la1[14] = jj_gen;
        break label_5;
      }
      nameToken = jj_consume_token(NAME);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 37:
        jj_consume_token(37);
        break;
      default:
        jj_la1[15] = jj_gen;
        ;
      }
                String name = nameToken.image;
            names.add(name);
    }
        {if (true) return names;}
    throw new Error("Missing return statement in function");
  }

  final public Set<String> parseInitialState() throws ParseException {
    Token nameToken;
    Set<String> highSignals = new HashSet<String>();
    label_6:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NAME:
      case 38:
        ;
        break;
      default:
        jj_la1[16] = jj_gen;
        break label_6;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 38:
        jj_consume_token(38);
        jj_consume_token(NAME);
        break;
      case NAME:
        nameToken = jj_consume_token(NAME);
                String name = nameToken.image;
                    highSignals.add(name);
        break;
      default:
        jj_la1[17] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 37:
        jj_consume_token(37);
        break;
      default:
        jj_la1[18] = jj_gen;
        ;
      }
    }
        {if (true) return highSignals;}
    throw new Error("Missing return statement in function");
  }

  final public List<Assign> parseAssigns() throws ParseException {
        Assign assign;
    List<Assign> assigns = new LinkedList<Assign>();
    label_7:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ASSIGN:
        ;
        break;
      default:
        jj_la1[19] = jj_gen;
        break label_7;
      }
      assign = parseAssign();
                assigns.add(assign);
    }
        {if (true) return assigns;}
    throw new Error("Missing return statement in function");
  }

  final public Assign parseAssign() throws ParseException {
    Token nameToken;
    Token formulaToken;
    jj_consume_token(ASSIGN);
    nameToken = jj_consume_token(NAME);
    formulaToken = jj_consume_token(FORMULA);
        String formula = formulaToken.image.replaceAll("^=", "").replaceAll(";$", "");
        {if (true) return new Assign(nameToken.image, formula);}
    throw new Error("Missing return statement in function");
  }

  final public List<Instance> parseInstances() throws ParseException {
    Instance instance;
    List<Instance> instances = new LinkedList<Instance>();
    label_8:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NAME:
      case PETRIFY_NAME:
        ;
        break;
      default:
        jj_la1[20] = jj_gen;
        break label_8;
      }
      instance = parseInstance();
            instances.add(instance);
    }
        {if (true) return instances;}
    throw new Error("Missing return statement in function");
  }

  final public Instance parseInstance() throws ParseException {
    String moduleName;
    Token nameToken = null;
    List<String> parameters;
    List<Pin> pins;
    moduleName = parseModuleName();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NAME:
      nameToken = jj_consume_token(NAME);
      break;
    default:
      jj_la1[21] = jj_gen;
      ;
    }
    jj_consume_token(33);
    pins = parsePins();
    jj_consume_token(34);
    jj_consume_token(39);
        String name = (nameToken == null ? null : nameToken.image);
        {if (true) return new Instance(name, moduleName, pins);}
    throw new Error("Missing return statement in function");
  }

  final public List<Pin> parsePins() throws ParseException {
    List<Pin> pins;
    if (jj_2_2(2147483647)) {
      pins = parseNamedPins();
    } else {
      pins = parseOrderedPins();
    }
        {if (true) return pins;}
    throw new Error("Missing return statement in function");
  }

  final public List<Pin> parseNamedPins() throws ParseException {
    Pin pin;
    List<Pin> pins = new LinkedList<Pin>();
    label_9:
    while (true) {
      pin = parseNamedPin();
            pins.add(pin);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 40:
        ;
        break;
      default:
        jj_la1[22] = jj_gen;
        break label_9;
      }
    }
        {if (true) return pins;}
    throw new Error("Missing return statement in function");
  }

  final public Pin parseNamedPin() throws ParseException {
    Token portName;
    Token netName;
    jj_consume_token(40);
    portName = jj_consume_token(NAME);
    jj_consume_token(33);
    netName = jj_consume_token(NAME);
    jj_consume_token(34);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 37:
      jj_consume_token(37);
      break;
    default:
      jj_la1[23] = jj_gen;
      ;
    }
        {if (true) return new Pin(portName.image, netName.image);}
    throw new Error("Missing return statement in function");
  }

  final public List<Pin> parseOrderedPins() throws ParseException {
    List<String> wires;
    List<Pin> pins = new LinkedList<Pin>();
    wires = parseNames();
        for (String wire: wires) {
                Pin pin = new Pin(null, wire);
                pins.add(pin);
        }
        {if (true) return pins;}
    throw new Error("Missing return statement in function");
  }

  private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  private boolean jj_2_2(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_2(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(1, xla); }
  }

  private boolean jj_3R_19() {
    if (jj_scan_token(OUTPUT)) return true;
    return false;
  }

  private boolean jj_3R_11() {
    Token xsp;
    if (jj_3R_13()) return true;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_13()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  private boolean jj_3R_14() {
    if (jj_3R_16()) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(37)) jj_scanpos = xsp;
    return false;
  }

  private boolean jj_3R_18() {
    if (jj_scan_token(INPUT)) return true;
    return false;
  }

  private boolean jj_3R_12() {
    if (jj_scan_token(33)) return true;
    Token xsp;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3R_14()) { jj_scanpos = xsp; break; }
    }
    if (jj_scan_token(34)) return true;
    return false;
  }

  private boolean jj_3R_15() {
    if (jj_scan_token(40)) return true;
    if (jj_scan_token(NAME)) return true;
    if (jj_scan_token(33)) return true;
    if (jj_scan_token(NAME)) return true;
    if (jj_scan_token(34)) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(37)) jj_scanpos = xsp;
    return false;
  }

  private boolean jj_3R_17() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_18()) {
    jj_scanpos = xsp;
    if (jj_3R_19()) return true;
    }
    return false;
  }

  private boolean jj_3R_10() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_12()) jj_scanpos = xsp;
    if (jj_scan_token(39)) return true;
    return false;
  }

  private boolean jj_3_2() {
    if (jj_3R_11()) return true;
    return false;
  }

  private boolean jj_3R_16() {
    if (jj_3R_17()) return true;
    if (jj_scan_token(NAME)) return true;
    return false;
  }

  private boolean jj_3_1() {
    if (jj_3R_10()) return true;
    return false;
  }

  private boolean jj_3R_13() {
    if (jj_3R_15()) return true;
    return false;
  }

  /** Generated Token Manager. */
  public VerilogParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private Token jj_scanpos, jj_lastpos;
  private int jj_la;
  private int jj_gen;
  final private int[] jj_la1 = new int[24];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x2000,0x80000000,0x40000000,0x0,0xa00000,0x0,0x18000,0x0,0x0,0x18000,0x0,0xf8000,0xe0000,0xf8000,0x200000,0x0,0x200000,0x200000,0x0,0x100000,0xa00000,0x200000,0x0,0x0,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0x0,0x0,0x0,0x1,0x0,0x82,0x0,0x20,0x2,0x0,0x2,0x0,0x0,0x0,0x0,0x20,0x40,0x40,0x20,0x0,0x0,0x0,0x100,0x20,};
   }
  final private JJCalls[] jj_2_rtns = new JJCalls[2];
  private boolean jj_rescan = false;
  private int jj_gc = 0;

  /** Constructor with InputStream. */
  public VerilogParser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public VerilogParser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new VerilogParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 24; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 24; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor. */
  public VerilogParser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new VerilogParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 24; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 24; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor with generated Token Manager. */
  public VerilogParser(VerilogParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 24; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(VerilogParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 24; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  static private final class LookaheadSuccess extends java.lang.Error { }
  final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;
  private int[] jj_lasttokens = new int[100];
  private int jj_endpos;

  private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      jj_entries_loop: for (java.util.Iterator<?> it = jj_expentries.iterator(); it.hasNext();) {
        int[] oldentry = (int[])(it.next());
        if (oldentry.length == jj_expentry.length) {
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              continue jj_entries_loop;
            }
          }
          jj_expentries.add(jj_expentry);
          break jj_entries_loop;
        }
      }
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[41];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 24; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 41; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

  private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 2; i++) {
    try {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
            case 1: jj_3_2(); break;
          }
        }
        p = p.next;
      } while (p != null);
      } catch(LookaheadSuccess ls) { }
    }
    jj_rescan = false;
  }

  private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }

}
