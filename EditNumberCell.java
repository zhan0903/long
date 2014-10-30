/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocado.rangeui.client.gui.component.grid.column;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.text.shared.SimpleSafeHtmlRenderer;
import static java.lang.Integer.parseInt;

/**
 *
 * @author zhan
 */
public class EditNumberCell extends
            AbstractEditableCell<Number, EditNumberCell.ViewData> {


private final NumberFormat format;

//    private Number Number(String value) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

 
  interface Template extends SafeHtmlTemplates {
    @Template("<input type=\"text\" value=\"{0}\" tabindex=\"-1\"></input>")
    SafeHtml input(String value);
  }
    static class ViewData {
 
    private boolean isEditing;
 
    /**
     * If true, this is not the first edit.
     */
    private boolean isEditingAgain;
 
    /**
     * Keep track of the original value at the start of the edit, which might be
     * the edited value from the previous edit and NOT the actual value.
     */
    private Number original;
 
    private Number num;
 
    /**
     * Construct a new ViewData in editing mode.
     *
     * @param num the Number to edit
     */
    public ViewData(Number num) {
      this.original = num;
      this.num = num;
      this.isEditing = true;
      this.isEditingAgain = false;
    }
 
    @Override
    public boolean equals(Object o) {
      if (o == null) {
        return false;
      }
      ViewData vd = (ViewData) o;
      return equalsOrBothNull(original, vd.original)
          && equalsOrBothNull(num, vd.num) && isEditing == vd.isEditing
          && isEditingAgain == vd.isEditingAgain;
    }
 
    public Number getOriginal() {
      return original;
    }
 
    public Number getNumber() {
      return num;
    }
 
    @Override
    public int hashCode() {
      return original.hashCode() + num.hashCode()
          + Boolean.valueOf(isEditing).hashCode() * 29
          + Boolean.valueOf(isEditingAgain).hashCode();
    }
 
    public boolean isEditing() {
      return isEditing;
    }
 
    public boolean isEditingAgain() {
      return isEditingAgain;
    }
 
    public void setEditing(boolean isEditing) {
      boolean wasEditing = this.isEditing;
      this.isEditing = isEditing;
 
      // This is a subsequent edit, so start from where we left off.
      if (!wasEditing && isEditing) {
        isEditingAgain = true;
        original = num;
      }
    }
 
    public void setNumber(Number num) {
      this.num = num;
    }
 
    private boolean equalsOrBothNull(Object o1, Object o2) {
      return (o1 == null) ? o2 == null : o1.equals(o2);
    }
  }
 
  private static Template template;
 
  private final SafeHtmlRenderer<String> renderer;
 
  /**
   * Construct a new EditTextCell that will use a
   * {@link SimpleSafeHtmlRenderer}.
   */
  public EditNumberCell() {
     this(NumberFormat.getDecimalFormat(), SimpleSafeHtmlRenderer.getInstance());
  }
  
   public EditNumberCell(NumberFormat format) {
    this(format, SimpleSafeHtmlRenderer.getInstance());
  }
 
  /**
   * Construct a new EditTextCell that will use a given {@link SafeHtmlRenderer}
   * to render the value when not in edit mode.
   *
     * @param renderer
     * @return  */
//  public EditNumberCell(NumberFormat format, SafeHtmlRenderer<String> renderer) {
//    super("click", "keyup", "keydown", "blur");
//    if (template == null) {
//      template = GWT.create(Template.class);
//    }
//    if (format == null) {
//      throw new IllegalArgumentException("format == null");
//    }
//    if (renderer == null) {
//      throw new IllegalArgumentException("renderer == null");
//    }
//    this.format = format;
//    this.renderer = renderer;
//  }
  
   public EditNumberCell(SafeHtmlRenderer<String> renderer) {
    this(NumberFormat.getDecimalFormat(), renderer);
  }
 
      @Override
    public boolean isEditing(Context context, Element parent, Number value) {
       ViewData viewData = getViewData(context.getKey());
       return viewData == null ? false : viewData.isEditing();//To change body of generated methods, choose Tools | Templates.
    }

 public EditNumberCell(NumberFormat format, SafeHtmlRenderer<String> renderer) {
    super("click", "keyup", "keydown", "blur");
    if (template == null) {
      template = GWT.create(Template.class);
    }
    if (format == null) {
      throw new IllegalArgumentException("format == null");
    }
    if (renderer == null) {
      throw new IllegalArgumentException("renderer == null");
    }
    this.format = format;
    this.renderer = renderer;
  }
 

@Override
  public void onBrowserEvent(Context context, Element parent, Number value,
      NativeEvent event, ValueUpdater<Number> valueUpdater) {
    Object key = context.getKey();
    ViewData viewData = getViewData(key);
    if (viewData != null && viewData.isEditing()) {
      // Handle the edit event.
      editEvent(context, parent, value, viewData, event, valueUpdater);
    } else {
      String type = event.getType();
      int keyCode = event.getKeyCode();
      boolean enterPressed = "keyup".equals(type)
          && keyCode == KeyCodes.KEY_ENTER;
      if ("click".equals(type) || enterPressed) {
        // Go into edit mode.
        if (viewData == null) {
          viewData = new ViewData(value);
          setViewData(key, viewData);
        } else {
          viewData.setEditing(true);
        }
        edit(context, parent, value);
      }
    }
  }
 
  
      @Override
    public void render(Context context, Number value, SafeHtmlBuilder sb) {
          // Get the view data.
    Object key = context.getKey();
    ViewData viewData = getViewData(key);
    if (viewData != null && !viewData.isEditing() && value != null
        && value.equals(viewData.getNumber())) {
      clearViewData(key);
      viewData = null;
    }
 
    if (viewData != null) {
      Number number = viewData.getNumber();
      if (viewData.isEditing()) {
        /*
         * Do not use the renderer in edit mode because the value of a text
         * input element is always treated as text. SafeHtml isn't valid in the
         * context of the value attribute.
         */
        sb.append(template.input(format.format(number)));
      } else {
        // The user pressed enter, but view data still exists.
        sb.append(renderer.render(format.format(number)));
      }
    } else if (value != null) {
      sb.append(renderer.render(format.format(value)));
    } //To change body of generated methods, choose Tools | Templates.
    }
   

@Override
  public boolean resetFocus(Context context, Element parent, Number value) {
    if (isEditing(context, parent, value)) {
      getInputElement(parent).focus();
      return true;
    }
    return false;
  }
 
  /**
   * Convert the cell to edit mode.
   *
   * @param context the {@link Context} of the cell
   * @param parent the parent element
   * @param value the current value
   */
  protected void edit(Context context, Element parent, Number value) {
    setValue(context, parent, value);
    InputElement input = getInputElement(parent);
    input.focus();
    input.select();
  }
 
  /**
   * Convert the cell to non-edit mode.
   * 
   * @param context the context of the cell
   * @param parent the parent Element
   * @param value the value associated with the cell
   */
  private void cancel(Context context, Element parent, Number value) {
    clearInput(getInputElement(parent));
    setValue(context, parent, value);
  }
 
  /**
   * Clear selected from the input element. Both Firefox and IE fire spurious
   * onblur events after the input is removed from the DOM if selection is not
   * cleared.
   *
   * @param input the input element
   */
  private native void clearInput(Element input) /*-{
    if (input.selectionEnd)
      input.selectionEnd = input.selectionStart;
    else if ($doc.selection)
      $doc.selection.clear();
  }-*/;
 
  /**
   * Commit the current value.
   * 
   * @param context the context of the cell
   * @param parent the parent Element
   * @param viewData the {@link ViewData} object
   * @param valueUpdater the {@link ValueUpdater}
   */
  private void commit(Context context, Element parent, ViewData viewData,
      ValueUpdater<Number> valueUpdater) {
    Number value = updateViewData(parent, viewData, false);
    clearInput(getInputElement(parent));
    setValue(context, parent, viewData.getOriginal());
    if (valueUpdater != null) {
      valueUpdater.update(value);
    }
  }
 
  private void editEvent(Context context, Element parent, Number value,
      ViewData viewData, NativeEvent event, ValueUpdater<Number> valueUpdater) {
    String type = event.getType();
    boolean keyUp = "keyup".equals(type);
    boolean keyDown = "keydown".equals(type);
    if (keyUp || keyDown) {
      int keyCode = event.getKeyCode();
      if (keyUp && keyCode == KeyCodes.KEY_ENTER) {
        // Commit the change.
        commit(context, parent, viewData, valueUpdater);
      } else if (keyUp && keyCode == KeyCodes.KEY_ESCAPE) {
        // Cancel edit mode.
        Number originalText = viewData.getOriginal();
        if (viewData.isEditingAgain()) {
          viewData.setNumber(originalText);
          viewData.setEditing(false);
        } else {
          setViewData(context.getKey(), null);
        }
        cancel(context, parent, value);
      } else {
        // Update the text in the view data on each key.
        updateViewData(parent, viewData, true);
      }
    } else if ("blur".equals(type)) {
      // Commit the change. Ensure that we are blurring the input element and
      // not the parent element itself.
      EventTarget eventTarget = event.getEventTarget();
      if (Element.is(eventTarget)) {
        Element target = Element.as(eventTarget);
        if ("input".equals(target.getTagName().toLowerCase())) {
          commit(context, parent, viewData, valueUpdater);
        }
      }
    }
  }
 
  /**
   * Get the input element in edit mode.
   */
  private InputElement getInputElement(Element parent) {
    return parent.getFirstChild().<InputElement> cast();
  }
 
  /**
   * Update the view data based on the current value.
   *
   * @param parent the parent element
   * @param viewData the {@link ViewData} object to update
   * @param isEditing true if in edit mode
   * @return the new value
   */
  private Number updateViewData(Element parent, ViewData viewData,
      boolean isEditing) {
    InputElement input = (InputElement) parent.getFirstChild();
    Number value = parseInt(input.getValue());
    viewData.setNumber(value);
    viewData.setEditing(isEditing);
    return value;
  }
}
    
   

