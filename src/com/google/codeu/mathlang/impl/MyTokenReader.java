// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.codeu.mathlang.impl;

import java.io.IOException;

import com.google.codeu.mathlang.core.tokens.NameToken;
import com.google.codeu.mathlang.core.tokens.NumberToken;
import com.google.codeu.mathlang.core.tokens.StringToken;
import com.google.codeu.mathlang.core.tokens.SymbolToken;
import com.google.codeu.mathlang.core.tokens.Token;
import com.google.codeu.mathlang.parsing.TokenReader;

public final class MyTokenReader implements TokenReader {

  String source;
  int index;

  public MyTokenReader(String source) 
  {
    this.source = source;
  }

    @Override
  public Token next() throws IOException {
    char[] symbols = {'+','-',';','=','(',')','\u0000','\"','\''};
    try {
      char curr;
      do {
        curr = nextChar();
      }
      while (curr == '\n' || curr == ' ');
      if(Character.isLetter(curr)) {
        return new NameToken(name(curr));
      } else if (curr >= '0' && curr <= '9') {
        return new NumberToken(num(curr));
      }
      if (curr == '+') {
        return new SymbolToken('+');
      } if (curr == '-') {
        return new SymbolToken('-');
      } if (curr == ';') {
        return new SymbolToken(';');
      } if (curr == '=') {
        return new SymbolToken('=');
      } if (curr == ')') {
        return new SymbolToken(')');
      } if (curr == '(') {
        return new SymbolToken('(');
      } if (curr == '\u0000') {
        return null;
      } if (curr == '\"') {
        return new StringToken(string(curr));
      } 
    }
    catch(IOException exception) {
      exception.printStackTrace();
      throw new IOException("Error while parsing source");
    } 
    return null;
  }

  private void prev() {
    index --;
  }
  public char nextChar() throws IOException {
    try {
      if(index < source.length()) {
        return source.charAt(index ++);
      } else {
        return '\u0000';
      }
    }
    catch(Exception e) {
      throw new IOException("Error while parsing source");
    }
  }

  private String string(char c) throws IOException {
    String name = "";
    try {
      c = nextChar();
      while(c != '\u0000' && c != '\"') {
        name += c;
        c = nextChar();
      }
    }
    catch(IOException exception)
    {
      throw new IOException("Error while parsing source");
    }
    return name;
  }

  private String name(char c) throws IOException {
    String name = Character.toString(c);
    try {
      c = nextChar();
      while((Character.isLetter(c)) && (c != '\u0000') && (c != ' ') && (c != ';')) {
        name += c;
        c = nextChar();
      }
      if(!Character.isLetter(c)) {
        prev();
      }
    } catch(IOException e) {
      throw new IOException("Error while parsing source");
    }
    return name;
  } 

  private double num(char c) {
    String anum = Character.toString(c);
    return Double.parseDouble(anum);
  }
}
