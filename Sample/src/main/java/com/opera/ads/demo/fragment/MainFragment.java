package com.opera.ads.demo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.opera.ads.AdFormat;
import com.opera.ads.OperaAds;
import com.opera.ads.demo.R;
import com.opera.ads.demo.databinding.FragmentMainBinding;
import com.opera.ads.demo.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private static class MainRecyclerAdapter extends RecyclerView.Adapter<MainViewHolder> {
        @NonNull
        private final List<MainItem> mData;
        @Nullable
        private ItemClickListener mItemClickListener;

        public MainRecyclerAdapter(@NonNull List<MainItem> data) {
            mData = data;
        }

        @NonNull
        @Override
        public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_main_item, parent, false);
            return new MainViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
            holder.itemView.setBackgroundResource(R.drawable.ripple_background);
            MainItem item = mData.get(position);
            holder.formatTv.setText(item.adFormat);
            holder.placementTv.setText(item.placementId);
            holder.itemView.setOnClickListener(v -> {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public void setItemClickListener(@NonNull ItemClickListener listener) {
            mItemClickListener = listener;
        }
    }

    private static class MainViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        public TextView formatTv;
        @NonNull
        public TextView placementTv;
        
        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            formatTv = itemView.findViewById(R.id.item_format);
            placementTv = itemView.findViewById(R.id.item_placement);
        }
    }

    private static class MainItem {
        @NonNull
        private final String adFormat;
        @NonNull
        private final String placementId;

        MainItem(@NonNull String format, @NonNull String placementId) {
            this.adFormat = format;
            this.placementId = placementId;
        }
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    private FragmentMainBinding mMainBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainBinding = FragmentMainBinding.inflate(inflater, container, false);
        return mMainBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMainBinding.sdkVersion.setText(requireContext().getString(R.string.sdk_version, OperaAds.getVersion()));

        RecyclerView recyclerView = mMainBinding.rvMain;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<MainItem> items = new ArrayList<>();
        items.add(new MainItem("Native", Constant.getPlacementId(AdFormat.NATIVE, false)));
        items.add(new MainItem("Banner", Constant.getPlacementId(AdFormat.BANNER, false)));
        items.add(new MainItem("Interstitial", Constant.getPlacementId(AdFormat.INTERSTITIAL, false)));
        items.add(new MainItem("Rewarded", Constant.getPlacementId(AdFormat.REWARDED, false)));
        MainRecyclerAdapter adapter = getMainRecyclerAdapter(items);
        recyclerView.setAdapter(adapter);
    }

    @NonNull
    private MainRecyclerAdapter getMainRecyclerAdapter(List<MainItem> items) {
        MainRecyclerAdapter adapter = new MainRecyclerAdapter(items);
        adapter.setItemClickListener(position -> {
            MainItem item = items.get(position);
            int destination = 0;
            switch (item.adFormat) {
                case "Native":
                    destination = R.id.action_main_to_native;
                    break;
                case "Banner":
                    destination = R.id.action_main_to_banner;
                    break;
                case "Interstitial":
                    destination = R.id.action_main_to_interstitial;
                    break;
                case "Rewarded":
                    destination = R.id.action_main_to_rewarded;
                    break;
                default:
                    break;
            }
            Bundle bundle = new Bundle();
            bundle.putString("placementId", item.placementId);
            NavHostFragment.findNavController(MainFragment.this).navigate(destination, bundle);
        });
        return adapter;
    }
}
