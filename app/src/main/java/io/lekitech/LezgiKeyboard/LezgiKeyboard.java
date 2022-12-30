package io.lekitech.LezgiKeyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

public class LezgiKeyboard extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private KeyboardView keyboardView;
    private Keyboard keyboard;
    int key_family = 0;
    int first_time_selected = 0;
    boolean shift_selected = false;
    boolean number_selected = false;
    boolean symbol_selected = false;

    @Override
    public View onCreateInputView() {
        keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this, R.xml.qwerty);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(this);
        return keyboardView;
    }

    @Override
    public void onPress(int i) {

    }

    @Override
    public void onRelease(int i) {

    }

    private void switchKeyboard(InputConnection inputConnection, int i) {
        switch (i) {
            case -1:
                if (shift_selected) {
                    shift_selected = false;
                    keyboard = new Keyboard(this, R.xml.qwerty);
                    keyboardView.setKeyboard(keyboard);
                    keyboardView.setOnKeyboardActionListener(this);
                } else {
                    shift_selected = true;
                    keyboard = new Keyboard(this, R.xml.shift_qwerty);
                    keyboardView.setKeyboard(keyboard);
                    keyboardView.setOnKeyboardActionListener(this);
                }
                break;
            case -2:
                if (number_selected) {
                    number_selected = false;
                    keyboard = new Keyboard(this, R.xml.qwerty);
                    keyboardView.setKeyboard(keyboard);
                    keyboardView.setOnKeyboardActionListener(this);
                } else {
                    number_selected = true;
                    keyboard = new Keyboard(this, R.xml.numbers_symbols);
                    keyboardView.setKeyboard(keyboard);
                    keyboardView.setOnKeyboardActionListener(this);
                }
                break;

            case -9:
                if (symbol_selected) {
                    symbol_selected = false;
                    keyboard = new Keyboard(this, R.xml.numbers_symbols);
                    keyboardView.setKeyboard(keyboard);
                    keyboardView.setOnKeyboardActionListener(this);
                } else {
                    symbol_selected = true;
                    keyboard = new Keyboard(this, R.xml.numbers_symbols_2);
                    keyboardView.setKeyboard(keyboard);
                    keyboardView.setOnKeyboardActionListener(this);
                }
                break;
        }
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection inputConnection = getCurrentInputConnection();
        switchKeyboard(inputConnection, primaryCode); //метод переключения клавиатуры (шифт, до символы)
        playClick(primaryCode);
        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE: // активация кнопки BACKSPACE
                inputConnection.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_DONE:
                inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            default:
                if (primaryCode == -2 || primaryCode == -1) {
                    return;
                }
//                System.out.println(primaryCode);
                System.out.println(keyCodes[0]);
                System.out.println(keyCodes[1]);
                if (keyCodes[1] > 0) {
                    char symbolOne = (char) keyCodes[0];
                    char symbolTwo = (char) keyCodes[1];
                    String one = String.valueOf(symbolOne);
                    String two = String.valueOf(symbolTwo);
                    inputConnection.commitText(one + two, 1);
                    //  inputConnection.commitText(two, 1);
                    return;
                }
                char c = (char) primaryCode;
                inputConnection.commitText(String.valueOf(c), 1);
        }
    }

    private void playClick(int i) {
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        switch (i) {
            default:
                audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }

    @Override
    public void onText(CharSequence charSequence) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
}