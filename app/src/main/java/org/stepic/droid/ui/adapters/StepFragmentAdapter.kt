package org.stepic.droid.ui.adapters

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import org.stepic.droid.model.Lesson
import org.stepic.droid.model.Step
import org.stepic.droid.model.Unit
import org.stepic.droid.util.AppConstants
import org.stepic.droid.util.resolvers.StepTypeResolver

class StepFragmentAdapter(fm: FragmentManager, val mStepList: List<Step?>, val mLesson: Lesson?, val mUnit: Unit?, val stepTypeResolver: StepTypeResolver) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        val step = mStepList[position]
        val fragment = stepTypeResolver.getFragment(step)
        val args = Bundle()
        args.putParcelable(AppConstants.KEY_STEP_BUNDLE, step)
        args.putParcelable(AppConstants.KEY_LESSON_BUNDLE, mLesson)
        args.putParcelable(AppConstants.KEY_UNIT_BUNDLE, mUnit)
        fragment.arguments = args
        return fragment
    }

    override fun getCount(): Int {
        return mStepList.size
    }

    fun getTabDrawable(position: Int): Drawable? {
        if (position >= mStepList.size) return null
        val step = mStepList[position]
        return stepTypeResolver.getDrawableForType(step?.block?.name, step?.is_custom_passed ?: false, step?.actions?.do_review ?: null != null)
    }
}
