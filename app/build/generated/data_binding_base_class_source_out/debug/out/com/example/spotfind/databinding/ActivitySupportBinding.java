// Generated by view binder compiler. Do not edit!
package com.example.spotfind.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.spotfind.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivitySupportBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button buttonChatbot;

  @NonNull
  public final LinearLayout main;

  @NonNull
  public final RecyclerView recyclerViewFaq;

  @NonNull
  public final TextView textViewFaq;

  @NonNull
  public final TextView tvSupport;

  private ActivitySupportBinding(@NonNull LinearLayout rootView, @NonNull Button buttonChatbot,
      @NonNull LinearLayout main, @NonNull RecyclerView recyclerViewFaq,
      @NonNull TextView textViewFaq, @NonNull TextView tvSupport) {
    this.rootView = rootView;
    this.buttonChatbot = buttonChatbot;
    this.main = main;
    this.recyclerViewFaq = recyclerViewFaq;
    this.textViewFaq = textViewFaq;
    this.tvSupport = tvSupport;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivitySupportBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivitySupportBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_support, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivitySupportBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.button_chatbot;
      Button buttonChatbot = ViewBindings.findChildViewById(rootView, id);
      if (buttonChatbot == null) {
        break missingId;
      }

      LinearLayout main = (LinearLayout) rootView;

      id = R.id.recyclerView_faq;
      RecyclerView recyclerViewFaq = ViewBindings.findChildViewById(rootView, id);
      if (recyclerViewFaq == null) {
        break missingId;
      }

      id = R.id.text_view_faq;
      TextView textViewFaq = ViewBindings.findChildViewById(rootView, id);
      if (textViewFaq == null) {
        break missingId;
      }

      id = R.id.tvSupport;
      TextView tvSupport = ViewBindings.findChildViewById(rootView, id);
      if (tvSupport == null) {
        break missingId;
      }

      return new ActivitySupportBinding((LinearLayout) rootView, buttonChatbot, main,
          recyclerViewFaq, textViewFaq, tvSupport);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
